package org.osehra.das.common.transformer;

import org.osehra.das.common.validation.NullChecker;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

public class BatchTransformer implements Transformer<Object, Object> {

	/**
	 * @uml.property  name="transformers"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.osehra.das.common.transformer.Transformer"
	 */
	List<Transformer<Object, Object>> transformers;

	@Required
	public void setTransformers(
			final List<Transformer<Object, Object>> transformers) {
		this.transformers = transformers;
	}

	@Override
	public Object transform(final Object src) throws TransformerException {
		if (NullChecker.isEmpty(this.transformers)) {
			return src;
		}

		Object value = src;
		for (final Transformer<Object, Object> transformer : this.transformers) {
			value = transformer.transform(value);
		}

		return value;
	}
}
