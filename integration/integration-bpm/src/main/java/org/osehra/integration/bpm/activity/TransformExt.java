package org.osehra.integration.bpm.activity;

import org.osehra.integration.bpm.engine.ActivityException;
import org.osehra.integration.bpm.engine.ProcessContext;
import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;

import org.springframework.beans.factory.annotation.Required;

/**
 * The transform activity, it uses a XSL message transformer to transform the
 * input.
 * 
 * @author Julian Jewel
 */
public class TransformExt extends AbstractTransform {

	Transformer<Object, Object> transformer;

	/**
	 * Execute the transform activity.
	 * 
	 * @param context
	 *            the process context
	 * @param source
	 *            the input source
	 * @throws ActivityException
	 *             an error occurred
	 * @return the transformed output
	 */
	@Override
	protected Object executeAction(final ProcessContext context,
			final Object source) throws ActivityException {
		try {
			return this.transformer.transform(source);
		} catch (final TransformerException ex) {
			throw new ActivityException(ex);
		}
	}

	@Required
	public void setTransformer(final Transformer<Object, Object> transformer) {
		this.transformer = transformer;
	}
}
