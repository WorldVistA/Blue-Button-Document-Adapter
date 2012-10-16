package org.osehra.integration.util.resolver.xml;

import org.osehra.integration.util.Assert;
import org.osehra.integration.util.resolver.Resolver;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

/**
 * Root name resource resolver. A map with root name of the document and the
 * resource is initialized. An input message with a matching root name will
 * return the resource from the hashtable.
 * 
 * @author Julian Jewel
 */
public class RootNameResourceResolver implements Resolver<Document, Resource> {

	/**
	 * Root name definitions are cached in this file.
	 * 
	 * @uml.property name="resources"
	 * @uml.associationEnd qualifier=
	 *                     "rootElementName:java.lang.String org.springframework.core.io.Resource"
	 */
	private java.util.Map<String, Resource> resources;

	/**
	 * Resolve the resource by root name based on the input document.
	 * 
	 * @param message
	 *            the input message DOM document
	 * @return the Spring resource
	 */
	@Override
	public Resource resolve(final Document message) {
		Assert.assertNotEmpty(message, "Document cannot be empty!");
		Assert.assertNotEmpty(message.getDocumentElement(),
				"Document element cannot be empty!");
		final String rootElementName = message.getDocumentElement()
				.getNodeName();
		Assert.assertNotEmpty(rootElementName,
				"Root element name cannot be empty!");
		Assert.assertFalse(!this.resources.containsKey(rootElementName),
				"Schema resource for " + rootElementName + " is missing!");
		final Resource resource = this.resources.get(rootElementName);
		return resource;
	}

	/**
	 * Set the resources.
	 * 
	 * @param theResources
	 *            the hashmap of Root name, Spring resource
	 */
	@Required
	public void setResources(
			final java.util.Map<String, Resource> theResources) {
		this.resources = theResources;
	}

}
