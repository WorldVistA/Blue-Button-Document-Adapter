package org.osehra.integration.jms.service;

import org.osehra.integration.core.component.Component;
import org.osehra.integration.core.router.Router;
import org.osehra.integration.core.router.RouterException;
import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.util.UUIDUtil;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.JmsTemplate;

/**
 * Asynchronous invoke a component.
 *
 * @author
 */
public class AsyncComponentServiceInvoker implements
		ServiceInvoker<Object, Object>, Router<Object, Object> {

	private Component<?, ?> component;

	private JmsTemplate inboundChannel;
	private JmsTemplate outboundChannel;

	@Override
	public Object invoke(final Object message)
			throws ServiceInvocationException {

		// The UOW identifier
		final String uowIdentifier = UUIDUtil.generateMessageId();
		// Component to invoke
		final String componentName = this.component.getName();

		final DynamicPropertiesMessagePostProcessor postProcessor = new DynamicPropertiesMessagePostProcessor();
		postProcessor.put(
				MessageHeaders.JMS_VA_Integration_Component.getProperty(),
				componentName);
		postProcessor.put(
				MessageHeaders.JMS_VA_Integration_Work_Id.getProperty(),
				uowIdentifier);
		// Send to UOW Queue (Note this queue should only handle Single
		// Message Delivery)
		this.inboundChannel.convertAndSend(message, postProcessor);
		// Receive (Handle aggregated delivery)
		final Object results = this.outboundChannel
				.receiveSelectedAndConvert(MessageHeaders.JMS_VA_Integration_Work_Id
						.getProperty() + "='" + uowIdentifier + "'");
		// Return results
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
	public void setComponent(final Component<?, ?> component) {
		this.component = component;
	}

	@Required
	public void setInboundChannel(final JmsTemplate inboundChannel) {
		this.inboundChannel = inboundChannel;
	}

	@Required
	public void setOutboundChannel(final JmsTemplate outboundChannel) {
		this.outboundChannel = outboundChannel;
	}
}
