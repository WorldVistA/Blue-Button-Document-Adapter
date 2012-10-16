package org.osehra.integration.core.transformer;

import org.osehra.integration.util.NullChecker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

public class CompositeTransformer implements Transformer<Object, Object> {

	/**
	 * @uml.property name="transformer"
	 * @uml.associationEnd
	 */
	Transformer<Object, Object> transformer;

	@Required
	public void setTransformer(final Transformer<Object, Object> transformer) {
		this.transformer = transformer;
	}

	@Override
	public Object transform(final Object list) throws TransformerException {
		if (NullChecker.isEmpty(list)) {
			return null;
		}

		final List<Object> results = new ArrayList<Object>();
		if (List.class.isInstance(list)) {
			for (final Object item : (List<?>) list) {
				final Object obj = this.transformer.transform(item);
				if (NullChecker.isNotEmpty(obj)) {
					results.add(obj);
				}
			}
			return results;
		} else {
			final Object obj = this.transformer.transform(list);
			return obj;
		}
	}
}
