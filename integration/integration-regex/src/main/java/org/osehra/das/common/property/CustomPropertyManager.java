package org.osehra.das.common.property;

/**
 * The custom property manager. Extracts the property from the input message.
 * 
 * @author Julian Jewel
 */
public interface CustomPropertyManager {

	/**
	 * Get the properties.
	 * 
	 * @param object
	 *            the input message
	 * @return the properties extracted from the message
	 */
	java.util.Map<String, Object> getProperties(Object object);
}
