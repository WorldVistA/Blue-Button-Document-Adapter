package org.osehra.integration.jms.transformer;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import javax.jms.Message;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * JMS message to XML document.
 * 
 * @author Julian Jewel
 */
public class JMSMessageToXML implements Transformer<Message, Document> {

	/**
	 * Message to String transformer.
	 * 
	 * @uml.property name="messageToString"
	 * @uml.associationEnd
	 */
	private Transformer<Message, String> messageToString;

	/**
	 * The XML to String transformer.
	 * 
	 * @uml.property name="stringToXML"
	 * @uml.associationEnd
	 */
	private Transformer<String, Document> stringToXML;

	/**
	 * Set the jms message to string transformer.
	 * 
	 * @param theMessageToString
	 *            the message to string transformer
	 */
	@Required
	public void setMessageToString(
			final Transformer<Message, String> theMessageToString) {
		this.messageToString = theMessageToString;
	}

	/**
	 * Set the XML to String transformer.
	 * 
	 * @param theStringToXML
	 *            the xml to string transformer
	 */
	@Required
	public void setStringToXML(
			final Transformer<String, Document> theStringToXML) {
		this.stringToXML = theStringToXML;
	}

	/**
	 * Transform JMS message to XML.
	 * 
	 * @param message
	 *            the JMS message
	 * @return the XML document
	 * @throws TransformerException
	 *             the exception when converting the JMS message to XML
	 */
	@Override
	public Document transform(final Message message)
			throws TransformerException {
		if (NullChecker.isEmpty(message)) {
			return null;
		}
		final String text = this.messageToString.transform(message);
		return this.stringToXML.transform(text);
	}
}
