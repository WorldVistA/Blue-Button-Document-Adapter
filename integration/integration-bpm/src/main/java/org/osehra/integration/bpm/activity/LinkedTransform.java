package org.osehra.integration.bpm.activity;

import org.osehra.integration.bpm.engine.ActivityException;
import org.osehra.integration.bpm.engine.ProcessContext;
import org.osehra.integration.util.NullChecker;
import org.osehra.integration.util.xsl.XSLTransformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * This activity takes in multiple transformers and executes them. Note that it
 * aggregates the result into an arraylist and filters empty values.
 * 
 * @author Julian Jewel
 */
public class LinkedTransform extends AbstractTransform {

	/**
	 * List of transformers.
	 * 
	 * @uml.property name="transformers"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     elementType="org.osehra.das.common.transformer.xsl.XSLTransformer"
	 */
	private List<XSLTransformer> transformers;

	/**
	 * Execute the transformers and add all the documents in an array list.
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
		final List<Object> documents = new ArrayList<Object>();
		for (final XSLTransformer transformer : this.transformers) {
			final Object result = this.transform(context, source, transformer);
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
		return documents;
	}

	/**
	 * Set the list of transformers.
	 * 
	 * @param theTransformers
	 *            the list of transformers
	 */
	@Required
	public void setTransformers(final List<XSLTransformer> theTransformers) {
		this.transformers = theTransformers;
	}
}
