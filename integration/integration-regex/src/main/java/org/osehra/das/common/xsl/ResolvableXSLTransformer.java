package org.osehra.das.common.xsl;

import org.osehra.das.common.resolver.Resolver;
import org.osehra.das.common.resolver.ResolverException;
import org.osehra.das.common.transformer.xsl.AbstractXSLTransformer;
import org.osehra.das.common.validation.Assert;

import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

/**
 * This class is an XSL transformer component that is used to perform XSL
 * transformations of cached stylesheets. The TransformerFactoryImpl caches each
 * stylesheet the first time it is created and used.
 */
public class ResolvableXSLTransformer extends AbstractXSLTransformer {
	/**
	 * The stylesheet resolver.
	 * @uml.property  name="stylesheetResolver"
	 * @uml.associationEnd  
	 */
	private Resolver<Document, Resource> stylesheetResolver;

	/**
	 * The stylesheet resolver.
	 * 
	 * @param resolver
	 *            the resolver
	 */
	// @Required
	public final void setResolver(final Resolver<Document, Resource> resolver) {
		this.stylesheetResolver = resolver;
	}

	/**
	 * Perform the actual transformation.
	 * 
	 * @param sourceMessage
	 *            - Transform this message content.
	 * @return the Result
	 * @throws TransformerException
	 *             an exception occured when transforming the source message
	 */
	@Override
	public final Result transform(final Source sourceMessage)
			throws TransformerException {
		return this.transform(sourceMessage, null);
	}

	/**
	 * Perform the actual transformation.
	 * 
	 * @param sourceMessage
	 *            - Transform this message content.
	 * @param targetMessage
	 *            - An initial transformed message - Not Used
	 * @return The Result
	 * @throws TransformerException
	 *             an exception occurred
	 */
	@Override
	public final Result transform(final Source sourceMessage,
			final Result targetMessage) throws TransformerException {
		return this.transform(sourceMessage, targetMessage, null);
	}

	/**
	 * Perform the actual transformation.
	 * 
	 * @param sourceMessage
	 *            - Transform this message content.
	 * @param targetMessage
	 *            - An initial transformed message - Not Used
	 * @param dynamicParameters
	 *            dynamic parameters to be set for a single tranformation
	 *            session
	 * @return the Result
	 * @throws TransformerException
	 *             an exception occurred
	 */
	@Override
	public final Result transform(final Source sourceMessage,
			final Result targetMessage,
			final Map<String, Object> dynamicParameters)
			throws TransformerException {
		try {
			// Get the resource
			Assert.assertInstance(sourceMessage, DOMSource.class);
			final Document root = (Document) ((DOMSource) sourceMessage)
					.getNode();
			Assert.assertNotEmpty(root, "Document cannot be empty");
			final Resource theResource = this.stylesheetResolver
					.resolve((Document) ((DOMSource) sourceMessage).getNode());
			return super.transform(theResource, sourceMessage, targetMessage,
					dynamicParameters);
		} catch (final ResolverException ex) {
			throw new RuntimeException(ex);
		}
	}
}
