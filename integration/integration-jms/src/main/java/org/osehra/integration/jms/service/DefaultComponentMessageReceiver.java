package org.osehra.integration.jms.service;

import org.osehra.integration.core.component.Component;
import org.osehra.integration.core.component.ComponentException;
import org.osehra.integration.core.interceptor.Interceptor;
import org.osehra.integration.core.interceptor.InterceptorException;
import org.osehra.integration.core.receiver.MessageReceiver;
import org.osehra.integration.core.receiver.MessageReceiverException;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

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
public class DefaultComponentMessageReceiver implements
		MessageReceiver<Object, Object>, MessageListener,
		ApplicationContextAware {

	ApplicationContext applicationContext;
	JmsTemplate replyToJmsTemplate;
	Component<Object, Object> component;
	Interceptor<Message, Message> interceptor;

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(final Message message) {
		if (NullChecker.isNotEmpty(this.interceptor)) {
			try {
				this.interceptor.intercept(message);
			} catch (InterceptorException ex) {
				throw new RuntimeException("Error on intereceptor",
						ex);
			}
		}

		try {
			Assert.assertNotEmpty(message, "Message cannot be null!");
			if (message
					.propertyExists(MessageHeaders.JMS_VA_Integration_Component
							.getProperty())) {
				final String componentName = (String) message
						.getObjectProperty(MessageHeaders.JMS_VA_Integration_Component
								.getProperty());
				final Object componentObj = this.applicationContext
						.getBean(componentName);
				Assert.assertInstance(component, Component.class);
				this.component = (Component<Object, Object>) componentObj;

			}

			if (NullChecker.isEmpty(component)) {
				throw new RuntimeException(
						MessageHeaders.JMS_VA_Integration_Component
								.getProperty()
								+ " property must be part of the message. Or Component needs to be defined.");
			}

			final Object result = component.processInbound(message);

			// Set the UOW message properties back
			final DynamicPropertiesMessagePostProcessor postProcessor = new DynamicPropertiesMessagePostProcessor();
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
			if (NullChecker.isNotEmpty(result)) {
				if (NullChecker.isNotEmpty(message.getJMSReplyTo())) {
					this.replyToJmsTemplate.convertAndSend(
							message.getJMSReplyTo(), result, postProcessor);
				} else {
					this.replyToJmsTemplate.convertAndSend(result,
							postProcessor);
				}
			}
		} catch (final ComponentException ex) {
			throw new RuntimeException(ex);
		} catch (final JMSException ex) {
			throw new RuntimeException(ex);
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

	public void setComponent(Component<Object, Object> component) {
		this.component = component;
	}

	public void setInterceptor(Interceptor<Message, Message> interceptor) {
		this.interceptor = interceptor;
	}
}
