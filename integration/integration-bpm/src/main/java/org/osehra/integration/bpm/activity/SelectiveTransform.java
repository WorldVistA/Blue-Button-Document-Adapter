package org.osehra.integration.bpm.activity;

import org.osehra.integration.bpm.engine.ActivityException;
import org.osehra.integration.bpm.engine.ProcessContext;
import org.osehra.integration.util.NullChecker;
import org.osehra.integration.util.xsl.XSLTransformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Required;

/**
 * This activity performs a transformation for each key item in the transformer
 * map that evaluates to true.
 * 
 * @author Keith Roberts
 * 
 */
public class SelectiveTransform extends AbstractTransform {

	/**
	 * Map of transformers.
	 * 
	 * @uml.property name="transformers"
	 * @uml.associationEnd qualifier=
	 *                     "ex:javax.xml.xpath.XPathExpression org.osehra.das.common.transformer.xsl.XSLTransformer"
	 */
	private final Map<XPathExpression, XSLTransformer> transformers = new LinkedHashMap<XPathExpression, XSLTransformer>();

	/**
	 * Executes the transformers whose key item evaluates to true and adds all
	 * the documents in an array list.
	 * 
	 * @param context
	 *            the process context
	 * @param source
	 *            the input source
	 * @return the aggregated output
	 * @throws ActivityException
	 *             an error occurred in transformation
	 */
	@Override
	protected final Object executeAction(final ProcessContext context,
			final Object source) throws ActivityException {
		try {
			final List<Object> documents = new ArrayList<Object>();
			final Iterator<Map.Entry<XPathExpression, XSLTransformer>> ci = this.transformers
					.entrySet().iterator();
			while (ci.hasNext()) {
				final Map.Entry<XPathExpression, XSLTransformer> entry = ci
						.next();
				final java.lang.Boolean booleanValue = (java.lang.Boolean) entry
						.getKey().evaluate(source, XPathConstants.BOOLEAN);
				if (NullChecker.isNotEmpty(booleanValue) && booleanValue) {
					final XSLTransformer transformer = entry.getValue();
					final Object result = this.transform(context, source,
							transformer);
					// Note: Ignores Empty results!
					if (List.class.isInstance(result)) {
						for (final Object obj : (List<?>) result) {
							if (NullChecker.isNotEmpty(result)) {
								documents.add(obj);
							}
						}
					} else if (NullChecker.isNotEmpty(result)) {
						documents.add(result);
					}
				}
			}
			return documents;
		} catch (final XPathExpressionException ex) {
			throw new ActivityException(ex);
		}
	}

	/**
	 * Sets the map of transformers.
	 * 
	 * @param theTransformers
	 *            .key - The XPath expression that will be evaluated.
	 * @param theTransformers
	 *            .value = The XSLTransformer that will be executed if the map
	 *            key evaluates to true.
	 */
	@Required
	public void setTransformers(
			final LinkedHashMap<String, XSLTransformer> theTransformers) {
		for (final java.util.Map.Entry<String, XSLTransformer> entry : theTransformers
				.entrySet()) {
			final XSLTransformer transformer = entry.getValue();
			final String key = entry.getKey();
			try {
				final XPathExpression ex = XPathFactory.newInstance()
						.newXPath().compile(key);
				this.transformers.put(ex, transformer);
			} catch (final XPathExpressionException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
