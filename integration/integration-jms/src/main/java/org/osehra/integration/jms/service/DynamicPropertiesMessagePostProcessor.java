package org.osehra.integration.jms.service;

import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.core.MessagePostProcessor;

/**
 * Properties that can be set on a JMS Message.
 * 
 * @author
 */
public class DynamicPropertiesMessagePostProcessor implements
		MessagePostProcessor {

	Map<String, Object> properties = new HashMap<String, Object>();

	public DynamicPropertiesMessagePostProcessor() {
	}

	@Override
	public Message postProcessMessage(final Message message)
			throws JMSException {
		Assert.assertNotEmpty(message, "Message cannot be null!");
		if (NullChecker.isNotEmpty(this.properties)) {
			for (final Entry<String, Object> entry : this.properties.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();

				if (NullChecker.isNotEmpty(value)) {
					message.setObjectProperty(key, value);
				}
			}
		}
		return message;
	}

	public void put(final String key, final Object value) {
		if (NullChecker.isNotEmpty(key) && NullChecker.isNotEmpty(value)) {
			this.properties.put(key, value);
		}
	}
	
	public Object get(final String key) {
		Object tmpObject = null;
		if (NullChecker.isNotEmpty(key)) {
			 tmpObject = this.properties.get(key);
		}
		return tmpObject;
	}
}
