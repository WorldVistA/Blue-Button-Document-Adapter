package org.osehra.integration.core.transformer.string;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

public class CollectionTransformer implements
		Transformer<String, Map<String, Object>> {

	/**
	 * @uml.property name="transformers"
	 * @uml.associationEnd qualifier=
	 *                     "key:java.lang.String org.osehra.das.common.transformer.Transformer"
	 */
	private Map<String, Transformer<String, Object>> transformers;

	@Required
	public void setTransformers(
			final Map<String, Transformer<String, Object>> transformers) {
		this.transformers = transformers;
	}

	@Override
	public Map<String, Object> transform(final String src)
			throws TransformerException {
		final Set<String> keySet = this.transformers.keySet();

		if (NullChecker.isEmpty(src)) {
			return null;
		}

		final Map<String, Object> propertyMap = new HashMap<String, Object>();
		for (final String key : keySet) {

			final Transformer<String, Object> transformer = this.transformers
					.get(key);
			final Object value = transformer.transform(src);
			propertyMap.put(key, value);
		}
		return propertyMap;
	}
}
