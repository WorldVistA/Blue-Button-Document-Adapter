package org.osehra.integration.util.property;

import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * XmlPropertyManager - takes in a document and extracts the properties out of
 * the message using XPath expressions.
 * 
 * @author Julian Jewel
 */
public class XmlPropertyManager implements CustomPropertyManager {

	/**
	 * The property expressions.
	 * 
	 * @uml.property name="propertyExpressions"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     elementType="javax.xml.xpath.XPathExpression"
	 *                     qualifier=
	 *                     "key:java.lang.String javax.xml.xpath.XPathExpression"
	 */
	private Map<String, XPathExpression> propertyExpressions;

	/**
	 * Get the properties extracted from the input document.
	 * 
	 * @param object
	 *            the input DOM document
	 * @return the map of String, Object
	 */
	@Override
	public Map<String, Object> getProperties(final Object object) {
		Assert.assertInstance(object, Document.class);
		final Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (final Entry<String, XPathExpression> entry : this.propertyExpressions
				.entrySet()) {
			try {
				final String value = (String) entry.getValue().evaluate(object,
						XPathConstants.STRING);
				if (NullChecker.isNotEmpty(value)) {
					map.put(entry.getKey(), value);
				}
			} catch (final XPathExpressionException ex) {
				throw new RuntimeException(
						"Could not run XPath when extracting properties from message!");
			}
		}
		return map;
	}

	/**
	 * Set the property expressions.
	 * 
	 * @param properties
	 *            the hash map of property key and string XPath expression
	 */
	@Required
	public void setpropertyExpressions(
			final Map<String, String> properties) {
		this.propertyExpressions = new Hashtable<String, XPathExpression>();
		for (final Entry<String, String> prop : properties.entrySet()) {
			try {
				this.propertyExpressions.put(prop.getKey(), XPathFactory
						.newInstance().newXPath().compile(prop.getValue()));
			} catch (final XPathExpressionException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
