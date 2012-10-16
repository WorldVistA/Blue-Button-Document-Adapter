package org.osehra.integration.util.resolver.xml;

import org.osehra.integration.util.resolver.Resolver;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

/**
 * Resolve a spring resource.
 * 
 * @author Asha Amritraj
 */
public class DefaultResourceResolver implements Resolver<Document, Resource> {

	/**
	 * The spring resource.
	 * 
	 * @uml.property name="resource"
	 * @uml.associationEnd
	 */
	private Resource resource;

	/**
	 * Resolve a Spring resource.
	 * 
	 * @param document
	 *            unused for now
	 * @return the Spring resource
	 */
	@Override
	public Resource resolve(final Document document) {
		return this.resource;
	}

	/**
	 * Set the spring resource.
	 * 
	 * @param theResource
	 *            the spring resource
	 * @uml.property name="resource"
	 */
	@Required
	public void setResource(final Resource theResource) {
		this.resource = theResource;
	}

}
