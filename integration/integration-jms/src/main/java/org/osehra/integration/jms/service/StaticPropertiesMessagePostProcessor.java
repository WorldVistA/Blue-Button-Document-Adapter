package org.osehra.integration.jms.service;

import java.util.Iterator;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.core.MessagePostProcessor;

public class StaticPropertiesMessagePostProcessor implements
		MessagePostProcessor {

	/**
	 * @uml.property name="properties"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.lang.String"
	 *                     qualifier="key:java.lang.String java.lang.Object"
	 */
	private Map<String, Object> properties;

	@Override
	public Message postProcessMessage(final Message message)
			throws JMSException {
		final Iterator<Map.Entry<String, Object>> ci = this.properties
				.entrySet().iterator();
		while (ci.hasNext()) {
			final Map.Entry<String, Object> entry = ci.next();
			final String name = entry.getKey();
			final Object value = entry.getValue();
			if (String.class.isInstance(value)) {
				final String str = (String) value;
				if (str.equalsIgnoreCase("true")
						|| str.equalsIgnoreCase("false")) {
					message.setBooleanProperty(name, Boolean.valueOf(str)
							.booleanValue());
				} else {
					message.setStringProperty(name, str);
				}
			} else {
				message.setObjectProperty(name, value);
			}
		}
		return message;
	}

	public void setProperties(final Map<String, Object> props) {
		this.properties = props;
	}

}
