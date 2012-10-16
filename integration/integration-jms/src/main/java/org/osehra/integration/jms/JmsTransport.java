package org.osehra.integration.jms;

import org.osehra.integration.util.NullChecker;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

/**
 * Jms Transport which uses a Spring JMS template to send/receive messages from
 * a queue.
 * 
 * @author Julian Jewel
 */
public class JmsTransport {

	/**
	 * The Spring JMS template.
	 * 
	 * @uml.property name="jmsTemplate"
	 * @uml.associationEnd
	 */
	private JmsTemplate jmsTemplate;

	/**
	 * The message post processor.
	 * 
	 * @uml.property name="messagePostProcessor"
	 * @uml.associationEnd
	 */
	private MessagePostProcessor messagePostProcessor;

	/**
	 * Receive a message from the queue.
	 * 
	 * @param source
	 *            the selector string with {1} as the message Id
	 * @return the received message
	 */
	public Object receive(final Object source) {
		return this.jmsTemplate.receiveAndConvert();
	}

	/**
	 * Receive a message from the queue.
	 * 
	 * @param theSelector
	 *            the selector string
	 * @return the received message
	 */
	public Object selectedReceive(final String theSelector) {
		return this.jmsTemplate.receiveSelectedAndConvert(theSelector);
	}

	/**
	 * Send the message to the specified destination.
	 * 
	 * @param object
	 *            the message.
	 * @param dest
	 *            The destination where the message is sent.
	 * @return the object
	 */
	public Object send(final Destination dest, final Object object) {
		if (NullChecker.isNotEmpty(this.messagePostProcessor)) {
			this.jmsTemplate.convertAndSend(dest, object,
					this.messagePostProcessor);
		} else {
			this.jmsTemplate.convertAndSend(dest, object);
		}
		return object;
	}

	/**
	 * Send the message to the queue.
	 * 
	 * @param object
	 *            the message
	 * @return the object
	 */
	public Object send(final Object object) {
		if (NullChecker.isNotEmpty(this.messagePostProcessor)) {
			this.jmsTemplate.convertAndSend(object, this.messagePostProcessor);
		} else {
			this.jmsTemplate.convertAndSend(object);
		}
		return object;
	}

	/**
	 * Set the JMS template.
	 * 
	 * @param theJmsTemplate
	 *            the jms template
	 * @uml.property name="jmsTemplate"
	 */
	@Required
	public void setJmsTemplate(final JmsTemplate theJmsTemplate) {
		this.jmsTemplate = theJmsTemplate;
	}

	/**
	 * Set the message post processor.
	 * 
	 * @param theMessagePostProcessor
	 *            the message post processor
	 * @uml.property name="messagePostProcessor"
	 */
	public void setMessagePostProcessor(
			final MessagePostProcessor theMessagePostProcessor) {
		this.messagePostProcessor = theMessagePostProcessor;
	}

}
