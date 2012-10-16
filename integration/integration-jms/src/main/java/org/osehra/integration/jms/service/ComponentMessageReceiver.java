package org.osehra.integration.jms.service;

import org.osehra.integration.core.component.Component;
import org.osehra.integration.core.component.ComponentException;
import org.osehra.integration.core.context.ThreadContext;
import org.osehra.integration.core.receiver.MessageReceiver;
import org.osehra.integration.core.receiver.MessageReceiverException;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;

/**
 * 
 * @author
 * 
 */
public class ComponentMessageReceiver implements
		MessageReceiver<Object, Object>, MessageListener,
		ApplicationContextAware {
	
	private static final Log LOG = LogFactory
			.getLog(ComponentMessageReceiver.class);

	ApplicationContext applicationContext;
	
	JmsTemplate replyToJmsTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(final Message message) {
		
		try {
			// verify an actual message is given as input
			Assert.assertNotEmpty(message, "Message cannot be null!");
			
			// verify the input message as the required 
			// JMS_VA_Integration_Component property, and if it does, 
			// get the component name which it contains
			if (!message
					.propertyExists(MessageHeaders.JMS_VA_Integration_Component
							.getProperty())) {
				throw new RuntimeException(
						MessageHeaders.JMS_VA_Integration_Component
								.getProperty()
								+ " property must be part of the message");
			}
			final String componentName = (String) message
					.getObjectProperty(MessageHeaders.JMS_VA_Integration_Component
							.getProperty());
			
			// use the retrieved component name to get the matching 
			// Component bean from the application, and assert it is 
			// a Component type instance 
			final Object component = this.applicationContext
					.getBean(componentName);			
			Assert.assertInstance(component, Component.class);
			
			// get the requestId 
			String requestId = "noRequestIdFound";
			if (message
					.propertyExists(MessageHeaders.JMS_VA_Request_Id
							.getProperty())) {
				requestId = (String) message.getObjectProperty(
						MessageHeaders.JMS_VA_Request_Id
						.getProperty());
			}
			
			// get this Message's sequenceId 
			String sequenceNo = "n";
			if (message
					.propertyExists(MessageHeaders.JMS_BEA_UnitOfWorkSequenceNumber
							.getProperty())) {
				Object obj = message.getObjectProperty(
						MessageHeaders.JMS_BEA_UnitOfWorkSequenceNumber
						.getProperty());
				sequenceNo = obj.toString();
			}
			
			if(LOG.isTraceEnabled()) {
				LOG.trace(requestId+"-"+sequenceNo+" onMessage() running, starting "
						+((Component<Object, Object>)component).getName()+" endpoint component...");
			}
			
			// get or create the threadContext bean for this thread 
			// and set the requestId-sequenceId into it for trace-logging
			ThreadContext threadContext = (ThreadContext) this.applicationContext
					.getBean("threadContext");
			threadContext.setRequestId(requestId+"-"+sequenceNo);
			
			// call the Component's processInbound() method to do 
			// the actual work and return the result require by the message 
			final Object result = ((Component<Object, Object>) component).processInbound(message);

			// Prepare the return message to send the message 
			// response back:  Set the UOW message properties back
			final DynamicPropertiesMessagePostProcessor postProcessor 
				= new DynamicPropertiesMessagePostProcessor();
			final Object uowIdentifier = message
					.getObjectProperty(MessageHeaders.JMS_BEA_UnitOfWork
							.getProperty());
			if (NullChecker.isNotEmpty(uowIdentifier)) {
				final Object uowSequenceNumber = message
						.getObjectProperty(MessageHeaders.JMS_BEA_UnitOfWorkSequenceNumber
								.getProperty());
				final Object uowIsWorkEnd = message
						.getObjectProperty(MessageHeaders.JMS_BEA_IsUnitOfWorkEnd
								.getProperty());
				postProcessor.put(
						MessageHeaders.JMS_BEA_UnitOfWork.getProperty(),
						uowIdentifier);
				postProcessor.put(
						MessageHeaders.JMS_BEA_UnitOfWorkSequenceNumber
								.getProperty(), uowSequenceNumber);
				postProcessor.put(
						MessageHeaders.JMS_BEA_IsUnitOfWorkEnd.getProperty(),
						uowIsWorkEnd);
			}			
			if (NullChecker.isNotEmpty(message.getJMSReplyTo())) {
				// get the return Destination from the original message 
				// and send the result/response message back to this 
				// specified Destination
				this.replyToJmsTemplate.convertAndSend(message.getJMSReplyTo(),
						result, postProcessor);
			} else {
				// send the result/response message back to the 
				// default Destination
				this.replyToJmsTemplate.convertAndSend(result, postProcessor);
			}
			if(LOG.isTraceEnabled()) {
				LOG.trace(((Component<Object, Object>)component).getName()+" endpoint component: "+requestId+"-"+sequenceNo+" onMessage() completing");
			}			
		} catch (final ComponentException ex) {	
			LOG.error("ComponentMessageReciever: onMessage(): "+ex.getMessage(), ex);
			throw new RuntimeException(ex);
		} catch (final JMSException ex) {
			LOG.error("ComponentMessageReciever: onMessage(): "+ex.getMessage(), ex);
			throw new RuntimeException(ex);
		} catch (final Throwable t) {
			LOG.error("ComponentMessageReciever: onMessage(): "+t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	@Override
	public Object receive(final Object e) throws MessageReceiverException {
		return e;
	}

	@Override
	public void setApplicationContext(
			final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Required
	public void setReplyToJmsTemplate(final JmsTemplate replyToJmsTemplate) {
		this.replyToJmsTemplate = replyToJmsTemplate;
	}
}
