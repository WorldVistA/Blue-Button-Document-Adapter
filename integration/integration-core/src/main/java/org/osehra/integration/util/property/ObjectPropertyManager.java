package org.osehra.integration.util.property;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Required;

/**
 * These are properties that can be set as string, object pair. For example
 * "some_property", "XYZ".
 * 
 * @author Julian Jewel
 */
public class ObjectPropertyManager implements CustomPropertyManager {

	/**
	 * Object properties.
	 * 
	 * @uml.property name="objectProperties"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     elementType="java.util.Map$Entry"
	 *                     qualifier="key:java.lang.Obect java.lang.Object"
	 */
	private Map<String, Object> objectProperties;

	/**
	 * Get the properties.
	 * 
	 * @param object
	 *            the input message.
	 * @return the properties HashMap "String, Object"
	 */
	@Override
	public Map<String, Object> getProperties(final Object object) {
		final Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (final Entry<String, Object> entry : this.objectProperties
				.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

	/**
	 * Set the properties.
	 * 
	 * @param theObjectProperties
	 *            the HashMap of String, Object
	 */
	@Required
	public void setObjectProperties(
			final Map<String, Object> theObjectProperties) {
		this.objectProperties = theObjectProperties;
	}
}
