package org.osehra.integration.jms.transformer;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Required;

/**
 * Convert from JMS message to Object.
 * 
 * @author Julian Jewel
 */
public class JMSMessageToObject implements Transformer<Message, Object> {
	/**
	 * JMS Message to String.
	 * 
	 * @uml.property name="messageToString"
	 * @uml.associationEnd
	 */
	private Transformer<Message, String> messageToString;

	/**
	 * Set JMS message to String.
	 * 
	 * @param theMessageToString
	 *            the message to String
	 */
	@Required
	public void setMessageToString(
			final Transformer<Message, String> theMessageToString) {
		this.messageToString = theMessageToString;
	}

	/**
	 * Transform JMS message to object.
	 * 
	 * @param message
	 *            the JMS message
	 * @return the object
	 * @throws TransformerException
	 *             exception when converting the JMS message to object
	 */
	@Override
	public Object transform(final Message message)
			throws TransformerException {
		if (NullChecker.isEmpty(message)) {
			return null;
		}

		try {
			// Remove Weblogic
			// if (XMLMessage.class.isInstance(message)) {
			// return ((XMLMessage) message).getDocument();
			// } else
			if (TextMessage.class.isInstance(message)) {
				return this.messageToString.transform(message);
			} else if (BytesMessage.class.isInstance(message)) {
				return this.messageToString.transform(message);
			} else if (ObjectMessage.class.isInstance(message)) {
				return ((ObjectMessage) message).getObject();
			}
		} catch (final JMSException ex) {
			throw new TransformerException(ex);
		}
		return null;
	}

}
