package org.osehra.integration.test.runner;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;

import org.springframework.beans.factory.annotation.Required;

public class StringTransformerTestRunner extends AbstractTestRunner {

	private Transformer<String, Object> transformer;

	@Override
	boolean isXmlInput() {
		return false;
	}

	@Override
	protected Object transduce() {
		Object result;
		try {
			result = this.transformer.transform((String) input);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * Sets the transformer that will perform the XSL transformation.
	 *
	 * @param transformer
	 *            - The transformer used to perform the transformation.
	 */
	@Required
	public void setTransformer(final Transformer<String, Object> transformer) {
		this.transformer = transformer;
	}

}
