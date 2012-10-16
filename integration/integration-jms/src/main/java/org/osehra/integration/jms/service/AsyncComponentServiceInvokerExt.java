package org.osehra.integration.jms.service;

import org.osehra.integration.core.component.Component;
import org.osehra.integration.core.context.ThreadContext;
import org.osehra.integration.core.router.Router;
import org.osehra.integration.core.router.RouterException;
import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.util.NullChecker;
import org.osehra.integration.util.UUIDUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.JmsTemplate;

/**
 * Asynchronously invoke services, enabled by using UOW, Queues, 
 * and server-managed threads.
 *
 * @author
 */
@SuppressWarnings("unchecked")
public class AsyncComponentServiceInvokerExt implements
		ServiceInvoker<Object, Object>, Router<Object, Object> {
		
	private ThreadContext threadContext;
	
	private List<Component<?, ?>> components;

	private JmsTemplate inboundChannel;
	private JmsTemplate outboundChannel;

	@Override
	public Object invoke(final Object message)
			throws ServiceInvocationException {
		
		Object results = null;
				
		// Get the current requestId
		String requestId = "noRequestId";
		if(NullChecker.isNotEmpty(this.threadContext)) {
			requestId = this.threadContext.getRequestId();			
		}
		// Set the UOW identifier
		final String uowIdentifier = UUIDUtil.generateMessageId();
		
		// Create a UOW Message Post Processor and set the MessageHeader values
		for (int i = 0; i < this.components.size(); i++) {
			final Component<?, ?> component = this.components.get(i);
			final String componentName = component.getName();

			final DynamicPropertiesMessagePostProcessor postProcessor 
				= new DynamicPropertiesMessagePostProcessor();
			postProcessor.put(
					MessageHeaders.JMS_VA_Integration_Component.getProperty(),
					componentName);
			postProcessor.put(
					MessageHeaders.JMS_VA_Integration_Work_Id.getProperty(),
					uowIdentifier);
			postProcessor.put(MessageHeaders.JMS_BEA_UnitOfWork.getProperty(),
					uowIdentifier);
			postProcessor.put(MessageHeaders.JMS_BEA_UnitOfWorkSequenceNumber
					.getProperty(), i + 1);
			postProcessor.put(MessageHeaders.JMS_VA_Request_Id
					.getProperty(), requestId);

			if (i + 1 == this.components.size()) {
				postProcessor.put(
						MessageHeaders.JMS_BEA_IsUnitOfWorkEnd.getProperty(),
						true);
			} else {
				postProcessor.put(
						MessageHeaders.JMS_BEA_IsUnitOfWorkEnd.getProperty(),
						false);
			}
			// Send Message and MessageHeaders to the UOW Queue for 
			// Endpoint Component(s) threads to be created and run
			// (Note: This queue should only handle Single
			// Message Delivery)
			this.inboundChannel.convertAndSend(message, postProcessor);
		}
		
		// Wait and receive all of the JMS messages in the UOW message group 
		// named by the uowIdentifier from the UOW Queue
		results = this.outboundChannel
				.receiveSelectedAndConvert(MessageHeaders.JMS_BEA_UnitOfWork
						.getProperty() + "='" + uowIdentifier + "'");
			
		// Check for and throw an exception for results == null when no 
		// results return, e.g. a queue time-out occurs 
		if (null == results) { 
			throw new ServiceInvocationException(
					"For RequestId: "+requestId
					+" no Feed results were returned from endpoint "
					+"components by the outboundChannel queue! "
					+"A queue time-out may have occurred, "
					+"or, a JMS Message error may have occurred."); 
		}	
		
		// See if an endpoint failed to provide either the default 
		// or a response Atom Feed
		if (((List<Object>)results).size() != this.components.size()) {
			throw new ServiceInvocationException(
					"For RequestId: "+requestId
					+" the number of Feed results rec'd is (" 
					+((List<Object>)results).size() 
					+"): number rec'd does " 
					+" not match the number of endpoints queried: "
					+this.components.size() + ": "
					+"this means one or more Feed results was not returned! "
					+"A JMS Message error may have occurred.");
		}
		
		return results;
	}

	@Override
	public Object route(final Object message) throws RouterException {
		try {
			return this.invoke(message);
		} catch (final ServiceInvocationException ex) {
			throw new RouterException(ex);
		}
	}

	@Required
	public void setComponents(final List<Component<?, ?>> components) {
		this.components = components;
	}

	@Required
	public void setInboundChannel(final JmsTemplate inboundChannel) {
		this.inboundChannel = inboundChannel;
	}

	@Required
	public void setOutboundChannel(final JmsTemplate outboundChannel) {
		this.outboundChannel = outboundChannel;
	}

	public void setThreadContext(ThreadContext threadContext) {
		this.threadContext = threadContext;
	}
}
