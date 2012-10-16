package org.osehra.integration.jms.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import org.osehra.integration.core.interceptor.Interceptor;
import org.osehra.integration.core.interceptor.InterceptorException;
import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

public class JmsLoggerInterceptor implements Interceptor<Message, Message> {

	private static final Log LOG = LogFactory
			.getLog(JmsLoggerInterceptor.class);

	Transformer<Message, String> jmsMessageToString;

	@Override
	public Message intercept(Message message) throws InterceptorException {
		if (LOG.isInfoEnabled()) {
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

				LOG.info("=============================================================");
				LOG.info("JMS Destination:" + strJmsDestination);
				LOG.info("JMS ReplyTo Destination:"
						+ strJmsReplyToDestination);
				for (Entry<String, String> propertySet : strProperties
						.entrySet()) {

					LOG.info(propertySet.getKey() + ":"
							+ propertySet.getValue());
				}
				LOG.info("JMS Message ID:"
						+ message.getJMSMessageID());
				LOG.info("JMS Correlation ID:"
						+ message.getJMSCorrelationID());
				LOG.info("JMS Message Content:" + jmsContent);
				LOG.info("=============================================================");

			} catch (JMSException ex) {
				throw new InterceptorException(ex);
			} catch (TransformerException ex) {
				throw new InterceptorException(ex);
			}
		}
		return message;
	}

	@Required
	public void setJmsMessageToString(
			Transformer<Message, String> jmsMessageToString) {
		this.jmsMessageToString = jmsMessageToString;
	}
}
