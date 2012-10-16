package org.osehra.integration.jms.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Required;

import org.osehra.integration.core.interceptor.Interceptor;
import org.osehra.integration.core.interceptor.InterceptorException;
import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

public class JmsConsoleInterceptor implements Interceptor<Message, Message> {

	Transformer<Message, String> jmsMessageToString;

	@Override
	public Message intercept(Message message) throws InterceptorException {
		try {
			String jmsContent = this.jmsMessageToString.transform(message);
			Destination jmsDestination = message.getJMSDestination();
			String strJmsDestination = "";
			if (NullChecker.isNotEmpty(jmsDestination)) {
				strJmsDestination = jmsDestination.toString();
			}
			Destination jmsReplyToDestination = message.getJMSReplyTo();
			String strJmsReplyToDestination = "";
			if (NullChecker.isNotEmpty(jmsReplyToDestination)) {
				strJmsReplyToDestination = jmsReplyToDestination.toString();
			}
			Map<String, String> strProperties = new HashMap<String, String>();
			Enumeration<String> properties = message.getPropertyNames();
			while (properties.hasMoreElements()) {
				String property = properties.nextElement();
				if (message.propertyExists(property)) {
					Object obj = message.getObjectProperty(property);
					if (NullChecker.isNotEmpty(obj)) {
						strProperties.put(property, obj.toString());
					}
				}
			}

			System.out
					.println("=============================================================");
			System.out.println("JMS Destination:" + strJmsDestination);
			System.out.println("JMS ReplyTo Destination:"
					+ strJmsReplyToDestination);
			for (Entry<String, String> propertySet : strProperties.entrySet()) {

				System.out.println(propertySet.getKey() + ":"
						+ propertySet.getValue());
			}
			System.out.println("JMS Message ID:" + message.getJMSMessageID());
			System.out.println("JMS Correlation ID:"
					+ message.getJMSCorrelationID());
			System.out.println("JMS Message Content:" + jmsContent);
			System.out
					.println("=============================================================");

		} catch (JMSException ex) {
			throw new InterceptorException(ex);
		} catch (TransformerException ex) {
			throw new InterceptorException(ex);
		}
		return message;
	}

	@Required
	public void setJmsMessageToString(
			Transformer<Message, String> jmsMessageToString) {
		this.jmsMessageToString = jmsMessageToString;
	}
}
