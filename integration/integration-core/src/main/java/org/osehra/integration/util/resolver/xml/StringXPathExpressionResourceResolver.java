package org.osehra.integration.util.resolver.xml;

import org.osehra.integration.util.Assert;
import org.osehra.integration.util.resolver.Resolver;
import org.osehra.integration.util.resolver.ResolverException;
import org.osehra.integration.util.selector.SelectorException;
import org.osehra.integration.util.selector.xml.XPathExpressionSelector;

import java.util.Map;

import javax.xml.xpath.XPathConstants;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

/**
 * This implements a resource resolver that selects a resource based on message
 * source data.
 * 
 * @author Keith Roberts
 */
public class StringXPathExpressionResourceResolver extends
		XPathExpressionSelector implements Resolver<Document, Resource> {

	/**
	 * The resources with a boolean XPathExpression as the key and the resource
	 * as the value. If the XPath expression evaluates to true, then the
	 * resource will be returned.
	 * 
	 * @uml.property name="resources"
	 * @uml.associationEnd 
	 *                     qualifier="value:java.lang.String org.springframework.core.io.Resource"
	 */
	private Map<String, Resource> resources;

	public StringXPathExpressionResourceResolver() {
		super.setReturnType(XPathConstants.STRING);
	}

	/**
	 * Gets the lookup key base on the selection of data from the dataSource
	 * using XPath selection expressions.
	 * 
	 * @param sourceData
	 *            the DOM Document
	 * @return the Spring resource
	 * @throws ResolverException
	 *             the exception when resolving a resource
	 */
	@Override
	public Resource resolve(final Document sourceData)
			throws ResolverException {
		try {
			Assert.assertNotEmpty(sourceData, "Source data cannot be empty!");
			Assert.assertInstance(sourceData, Document.class,
					"Has to be a DOM document");

			final String value = (String) super.select(sourceData);
			// Allow empty values
			if (this.resources.containsKey(value)) {
				return this.resources.get(value);
			}
			throw new RuntimeException("Unsupported document type!");

		} catch (final SelectorException ex) {
			throw new ResolverException(
					"Failed to resolve resource by XPathExpress", ex);
		}
	}

	/**
	 * Sets the resources from which a particular resource may be selected based
	 * on source message data content. The key value of the resource map must
	 * compile into a valid XPathExpression.
	 * 
	 * @param theResources
	 *            - A map of XPathExpressions and resources. The resource is
	 *            considered resolved if a particular XPathExpression evaluates
	 *            to true.
	 */
	@Required
	public void setResources(final Map<String, Resource> theResources) {
		this.resources = theResources;
	}

}
