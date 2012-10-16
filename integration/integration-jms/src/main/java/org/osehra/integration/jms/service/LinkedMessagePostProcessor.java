package org.osehra.integration.jms.service;

import org.osehra.integration.util.NullChecker;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.core.MessagePostProcessor;

/**
 * The component router. It routes the message to multiple components.
 * 
 * @author
 */
public class LinkedMessagePostProcessor implements MessagePostProcessor {

	MessagePostProcessor[] messagePostProcessors;

	public LinkedMessagePostProcessor(
			final MessagePostProcessor... postProcessors) {
		this.messagePostProcessors = postProcessors;
	}

	@Override
	public Message postProcessMessage(Message message) throws JMSException {
		if (NullChecker.isNotEmpty(this.messagePostProcessors)) {
			for (final MessagePostProcessor processor : this.messagePostProcessors) {
				message = processor.postProcessMessage(message);
			}
		}
		return message;
	}
}
