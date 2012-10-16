package org.osehra.integration.jms.service;

import org.osehra.integration.core.interceptor.Interceptor;
import org.osehra.integration.core.interceptor.InterceptorException;
import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;
import org.osehra.integration.util.property.CustomPropertyManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.w3c.dom.Document;

/**
 * A default message converter implementation. It converts the JMS messages to
 * XML or String objects. TODO: Replace transformers to be injected by Spring.
 * 
 * @author Julian Jewel
 */
public class MessageConverterImpl implements MessageConverter {

	/**
	 * If true, then remove extended ascii.
	 * 
	 * @uml.property name="cleanExtended"
	 */
	private boolean cleanExtended = false;
	/**
	 * Custom property managers help set the properties for the message.
	 * 
	 * @uml.property name="customPropertyManagers"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private List<CustomPropertyManager> customPropertyManagers;
	/**
	 * The from message type will convert any object to XML, Bytes or Object to
	 * construct a JMS message type. The default value is BYTES and Jms Byte
	 * messages are constructed.
	 * 
	 * @uml.property name="fromMessageType"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private MessageType fromMessageType = MessageType.BYTES;
	/**
	 * The from transformer would transform the message from the service.
	 * 
	 * @uml.property name="fromTransformer"
	 * @uml.associationEnd
	 */
	private Transformer<Object, Object> fromTransformer;
	/**
	 * Message to Object transformer.
	 * 
	 * @uml.property name="messageToObject"
	 * @uml.associationEnd
	 */
	private Transformer<Message, Object> messageToObject;
	/**
	 * Message to String transformer.
	 * 
	 * @uml.property name="messageToString"
	 * @uml.associationEnd
	 */
	private Transformer<Message, String> messageToString;
	/**
	 * Message to XML transformer.
	 * 
	 * @uml.property name="messageToXML"
	 * @uml.associationEnd
	 */
	private Transformer<Message, Document> messageToXML;
	/**
	 * The to message type will convert any object to the appropriate Jms
	 * message type. The default value is BYTES.
	 * 
	 * @uml.property name="toMessageType"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private MessageType toMessageType = MessageType.BYTES;
	/**
	 * The to transformer would transform the message to the service.
	 * 
	 * @uml.property name="toTransformer"
	 * @uml.associationEnd
	 */
	private Transformer<Object, Object> toTransformer;
	/**
	 * XML to String transformer.
	 * 
	 * @uml.property name="xmlToString"
	 * @uml.associationEnd
	 */
	private Transformer<Document, String> xmlToString;

	private Interceptor<Message, Message> jmsInterceptor;

	/**
	 * Convert JMS message to Object.
	 * 
	 * @param message
	 *            the JMS message
	 * @return the converted object
	 * @throws JMSException
	 *             an exception in conversion
	 */
	@Override
	public Object fromMessage(final Message message) throws JMSException {

		try {
			if (NullChecker.isNotEmpty(this.jmsInterceptor)) {
				try {
					this.jmsInterceptor.intercept(message);
				} catch (InterceptorException ex) {
					throw new MessageConversionException("Error on intereceptor",
							ex);
				}
			}
			// If it is a UOW message - recurse through the objects in the
			// message
			if (ObjectMessage.class.isInstance(message)) {
				final Object uowMessageObject = ((ObjectMessage) message)
						.getObject();
				// List of messages
				if (List.class.isInstance(uowMessageObject)) {
					final java.util.List<?> objects = (java.util.List<?>) uowMessageObject;
					final List<Object> results = new ArrayList<Object>();
					for (final Object jmsSingleMessage : objects) {
						if (Message.class.isInstance(jmsSingleMessage)) {
							// Add results
							results.add(this
									.fromMessage((Message) jmsSingleMessage));
						}
					}
					// UOW Results
					if (NullChecker.isNotEmpty(results)) {
						return results;
					}
				}
			}
			// Process messages
			Object object;
			if (MessageType.XML.equals(this.fromMessageType)) {
				object = this.messageToXML.transform(message);
			} else if (MessageType.BYTES.equals(this.fromMessageType)) {
				object = this.messageToString.transform(message);
			} else if (MessageType.OBJECT.equals(this.fromMessageType)) {
				object = this.messageToObject.transform(message);
			} else {
				throw new RuntimeException("Unsupported type!");
			}
			if (String.class.isInstance(object) && this.cleanExtended) {
				if (NullChecker.isNotEmpty(object)) {
					object = ((String) object).trim();
				}
			}
			if (NullChecker.isNotEmpty(this.fromTransformer)) {
				object = this.fromTransformer.transform(object);
			}
			return object;
		} catch (final TransformerException ex) {
			throw new MessageConversionException(
					"Error on transforming message", ex);
		}

	}

	/**
	 * Set clean extended parameter.
	 * 
	 * @param theCleanExtended
	 *            true to clean the string, false otherwise
	 * @uml.property name="cleanExtended"
	 */
	public void setCleanExtended(final boolean theCleanExtended) {
		this.cleanExtended = theCleanExtended;
	}

	/**
	 * Set the list of custom property managers.
	 * 
	 * @param theCustomPropertyManagers
	 *            the property managers
	 */
	public void setCustomPropertyManagers(
			final List<CustomPropertyManager> theCustomPropertyManagers) {
		this.customPropertyManagers = theCustomPropertyManagers;
	}

	/**
	 * Set the from message type.
	 * 
	 * @param theFromMessageType
	 *            the from message type
	 * @uml.property name="fromMessageType"
	 */
	public void setFromMessageType(final MessageType theFromMessageType) {
		this.fromMessageType = theFromMessageType;
	}

	/**
	 * Set the from transformer. This would transform the message from the
	 * service.
	 * 
	 * @param theFromTransformer
	 *            the from transformer
	 */
	public void setFromTransformer(
			final Transformer<Object, Object> theFromTransformer) {
		this.fromTransformer = theFromTransformer;
	}

	/**
	 * Set the jms message to object transformer.
	 * 
	 * @param theMessageToObject
	 *            the message to object transformer.
	 */
	@Required
	public void setMessageToObject(
			final Transformer<Message, Object> theMessageToObject) {
		this.messageToObject = theMessageToObject;
	}

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
	 * Set the jms message to XML transformer.
	 * 
	 * @param theMessageToXML
	 *            the jms message to XML transformer
	 */
	@Required
	public void setMessageToXML(
			final Transformer<Message, Document> theMessageToXML) {
		this.messageToXML = theMessageToXML;
	}

	/**
	 * The to message type. This would set the JMS message type to construct.
	 * 
	 * @param theToMessageType
	 *            the to message type
	 * @uml.property name="toMessageType"
	 */
	public void setToMessageType(final MessageType theToMessageType) {
		this.toMessageType = theToMessageType;
	}

	/**
	 * Set the transformer when sending a message to the service.
	 * 
	 * @param theToTransformer
	 *            the transformer when sending to the service
	 */
	public void setToTransformer(
			final Transformer<Object, Object> theToTransformer) {
		this.toTransformer = theToTransformer;
	}

	/**
	 * Set the XML to String transformer.
	 * 
	 * @param theXmlToString
	 *            the xml to string transformer
	 */
	@Required
	public void setXmlToString(
			final Transformer<Document, String> theXmlToString) {
		this.xmlToString = theXmlToString;
	}

	/**
	 * The converter when message is received from the service.
	 * 
	 * @param object
	 *            the input object to be converted to JMS message
	 * @param session
	 *            the JMS session
	 * @return the JMS Message
	 * @throws JMSException
	 *             when there is an error converting the message
	 */
	@Override
	public Message toMessage(final Object object, final Session session)
			throws JMSException {

		Message message;
		try {
			Object resultObject = object;

			if (NullChecker.isEmpty(resultObject)) {
				throw new RuntimeException("Valid object expected!");
			}

			if (NullChecker.isNotEmpty(this.toTransformer)) {
				resultObject = this.toTransformer.transform(resultObject);
				if (NullChecker.isEmpty(resultObject)) {
					throw new RuntimeException("Valid object expected!");
				}
			}
			if (MessageType.XML.equals(this.toMessageType)
					|| MessageType.TEXT.equals(this.toMessageType)) {
				if (Document.class.isInstance(resultObject)) {
					message = session.createTextMessage(this.xmlToString
							.transform((Document) resultObject));
				} else if (String.class.isInstance(resultObject)) {
					message = session.createTextMessage((String) resultObject);
				} else {
					throw new RuntimeException("Unsupported type!");
				}
			} else if (MessageType.BYTES.equals(this.toMessageType)) {
				String mStr;
				if (Document.class.isInstance(resultObject)) {
					mStr = this.xmlToString.transform((Document) resultObject);
				} else if (String.class.isInstance(resultObject)) {
					mStr = (String) resultObject;
				} else {
					throw new RuntimeException("Unsupported type!");
				}
				message = session.createBytesMessage();
				((BytesMessage) message).writeBytes(mStr.getBytes());
			} else if (MessageType.OBJECT.equals(this.toMessageType)) {
				if (Serializable.class.isInstance(resultObject)) {
					message = session
							.createObjectMessage((Serializable) resultObject);
				} else {
					throw new RuntimeException("Object has to be serializable!");
				}
			} else {
				throw new RuntimeException("Unsupported type!");
			}
			// Set the properties based on the custom property manager.
			if (NullChecker.isNotEmpty(this.customPropertyManagers)) {
				for (final CustomPropertyManager customPropertyManager : this.customPropertyManagers) {
					final Map<String, Object> properties = customPropertyManager
							.getProperties(resultObject);
					if (NullChecker.isNotEmpty(properties)) {
						for (final Entry<String, Object> property : properties
								.entrySet()) {
							if (NullChecker.isNotEmpty(property)
									&& NullChecker.isNotEmpty(property
											.getValue())) {
								// Special set for correlation identifiers since
								// WLS requires it to be set this way.
								if ("JMSCorrelationID"
										.equals(property.getKey())) {
									message.setJMSCorrelationID(property
											.getValue().toString());
								}
								// Special set for replyto since
								// WLS requires it to be set this way.
								else if ("JMSReplyTo".equals(property.getKey())) {
									message.setJMSReplyTo((javax.jms.Destination) property
											.getValue());

								} else {
									message.setObjectProperty(
											property.getKey(),
											property.getValue());
								}
							}
						}
					}
				}
			}

		} catch (final TransformerException ex) {
			throw new MessageConversionException("Error on transformation", ex);
		}
		if (NullChecker.isNotEmpty(this.jmsInterceptor)) {
			try {
				message = this.jmsInterceptor.intercept(message);
			} catch (InterceptorException ex) {
				throw new MessageConversionException("Error on intereceptor",
						ex);
			}
		}

		return message;
	}

	public void setJmsInterceptor(Interceptor<Message, Message> jmsInterceptor) {
		this.jmsInterceptor = jmsInterceptor;
	}
}
