package org.osehra.integration.util.xsl;

import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

/**
 * The abstract class for transforming using XSL.
 * 
 * @author Asha Amritraj
 */
public abstract class AbstractXSLTransformer implements
		ApplicationContextAware, XSLTransformer {

	public static void throwTransformerException(final String message)
			throws TransformerException {
		throw new TransformerException(message);
	}

	/**
	 * @uml.property name="errorListener"
	 * @uml.associationEnd
	 */
	private ErrorListener errorListener = null;
	/**
	 * @uml.property name="parameters"
	 * @uml.associationEnd qualifier=
	 *                     "name:java.lang.String org.springframework.context.ApplicationContext"
	 */
	private final Map<String, Object> parameters = new HashMap<String, Object>();
	/**
	 * @uml.property name="transformerFactory"
	 * @uml.associationEnd
	 */
	private TransformerFactory transformerFactory;

	/**
	 * @return
	 * @uml.property name="transformerFactory"
	 */
	public TransformerFactory getTransformerFactory() {
		return this.transformerFactory;
	}

	private void setAllParameters(final javax.xml.transform.Transformer trans,
			final Map<String, Object> theParameters) {
		if (NullChecker.isNotEmpty(theParameters)) {
			final Iterator<Map.Entry<String, Object>> ci = theParameters
					.entrySet().iterator();
			while (ci.hasNext()) {
				final Map.Entry<String, Object> entry = ci.next();
				trans.setParameter(entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
	public void setApplicationContext(
			final ApplicationContext applicationContext) {
		this.parameters.put("applicationContext", applicationContext);
	}

	/**
	 * @param listener
	 * @uml.property name="errorListener"
	 */
	public void setErrorListener(final ErrorListener listener) {
		this.errorListener = listener;
	}

	public void setParameter(final String name, final Object value) {
		if (NullChecker.isNotEmpty(name) && NullChecker.isNotEmpty(value)) {
			this.parameters.put(name, value);
		}
	}

	public void setParameters(
			final Map<String, ? extends Object> theParameters) {
		this.parameters.putAll(theParameters);
	}

	/**
	 * @param transformerFactory
	 * @uml.property name="transformerFactory"
	 */
	@Required
	public void setTransformerFactory(
			final TransformerFactory transformerFactory) {
		this.transformerFactory = transformerFactory;
	}

	protected final Result transform(final Resource resource,
			final Source sourceMessage) throws TransformerException {
		return this.transform(resource, sourceMessage, null);
	}

	protected final Result transform(final Resource resource,
			final Source sourceMessage, final Result targetMessage)
			throws TransformerException {
		return this.transform(resource, sourceMessage, targetMessage, null);
	}

	protected final Result transform(final Resource resource,
			final Source sourceMessage, final Result targetMessage,
			final Map<String, Object> dynamicParameters)
			throws TransformerException {
		Assert.assertNotEmpty(resource, "Resource cannot be null!");
		try {

			final Templates templates = this.transformerFactory
					.newTemplates(resource.getURL());
			final javax.xml.transform.Transformer trans = templates
					.newTransformer();
			if (this.errorListener != null) {
				trans.setErrorListener(this.errorListener);
			}
			this.setAllParameters(trans, this.parameters);
			this.setAllParameters(trans, dynamicParameters);
			trans.transform(sourceMessage, targetMessage);
			return targetMessage;
		} catch (final javax.xml.transform.TransformerException ex) {
			throw new TransformerException(ex);
		} catch (final IOException ex) {
			throw new TransformerException(ex);
		}
	}
}
