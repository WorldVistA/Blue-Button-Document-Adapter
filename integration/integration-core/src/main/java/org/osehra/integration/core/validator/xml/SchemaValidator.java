package org.osehra.integration.core.validator.xml;

import org.osehra.integration.core.validator.ValidatorException;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.resolver.Resolver;
import org.osehra.integration.util.resolver.ResolverException;

import java.io.IOException;

import javax.xml.validation.SchemaFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * The schema validator.
 * 
 * @author Julian Jewel
 */
public class SchemaValidator implements
		org.osehra.integration.core.validator.Validator<Document> {
	/**
	 * The error handler.
	 * 
	 * @uml.property name="errorHandler"
	 * @uml.associationEnd
	 */
	private ErrorHandler errorHandler;

	/**
	 * Set factory name for the schema factory.
	 * 
	 * @uml.property name="factory"
	 * @uml.associationEnd
	 */
	private SchemaFactory factory;

	/**
	 * The resource resolver.
	 * 
	 * @uml.property name="resolver"
	 * @uml.associationEnd
	 */
	private Resolver<Document, Resource> resolver;

	/**
	 * Set the error handler.
	 * 
	 * @param theErrorHandler
	 *            the error handler
	 * @uml.property name="errorHandler"
	 */
	public void setErrorHandler(final ErrorHandler theErrorHandler) {
		this.errorHandler = theErrorHandler;
	}

	/**
	 * Set the schema factory.
	 * 
	 * @param theFactory
	 *            the schema factory
	 * @uml.property name="factory"
	 */
	@Autowired
	public void setFactory(final SchemaFactory theFactory) {
		this.factory = theFactory;
	}

	/**
	 * Set the file resolver.
	 * 
	 * @param theResourceResolver
	 *            the resource resolver
	 */
	@Required
	public void setResolver(
			final Resolver<Document, Resource> theResourceResolver) {
		Assert.assertNotEmpty(theResourceResolver,
				"Resource Resolver cannot be null!");
		this.resolver = theResourceResolver;
	}

	/**
	 * Validate the document based on a schema (XSD).
	 * 
	 * @param message
	 *            the input message
	 * @return true if validation is successful, false otherwise
	 * @throws ValidatorException
	 *             if the message does not validate with the schema based on the
	 *             exceptionOnError flag in the error handler.
	 */
	@Override
	public boolean validate(final Document message)
			throws ValidatorException {

		Assert.assertNotEmpty(message, "Message cannot be null!");
		
		try {
			final Resource schemaResource = this.resolver.resolve(message);
			
			return SchemaValidationUtil.validate(message, schemaResource,
					this.errorHandler, this.factory);
			
		} catch (final ResolverException e) {
			throw new ValidatorException(e);		 
		} catch (final SAXException e) {
			throw new ValidatorException(e);
		} catch (final IOException e) {
			throw new ValidatorException(e);
		}
	}	
	
}
