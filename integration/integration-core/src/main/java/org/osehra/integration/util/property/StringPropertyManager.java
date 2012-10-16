package org.osehra.integration.util.property;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Required;

/**
 * These are properties that can be set as string, string pair. For example
 * "some_property", "XYZ".
 * 
 * @author Julian Jewel
 */
public class StringPropertyManager implements CustomPropertyManager {

	/**
	 * String properties.
	 * 
	 * @uml.property name="stringProperties"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     elementType="java.util.Map$Entry"
	 *                     qualifier="key:java.lang.String java.lang.String"
	 */
	private Map<String, String> stringProperties;

	/**
	 * Get the properties.
	 * 
	 * @param object
	 *            the input message.
	 * @return the properties HashMap "String, String"
	 */
	@Override
	public Map<String, Object> getProperties(final Object object) {
		final Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (final Entry<String, String> entry : this.stringProperties
				.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

	/**
	 * Set the properties.
	 * 
	 * @param theStringProperties
	 *            the HashMap of String, String
	 */
	@Required
	public void setStringProperties(
			final Map<String, String> theStringProperties) {
		this.stringProperties = theStringProperties;
	}
}
