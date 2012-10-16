package org.osehra.das.common.transformer.string;

import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.validation.NullChecker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

public class CollectionTransformerExt implements
		Transformer<Map<String, Object>, Map<String, Map<String, Object>>> {

	/**
	 * @uml.property  name="transformers"
	 * @uml.associationEnd  
	 */
	private Transformer<Object, Map<String, Object>> transformers;

	@Required
	public void setTransformers(
			final Transformer<Object, Map<String, Object>> transformers) {
		this.transformers = transformers;
	}

	@Override
	public Map<String, Map<String, Object>> transform(
			final Map<String, Object> collection) throws TransformerException {
		if (NullChecker.isEmpty(collection)) {
			return null;
		}

		final Map<String, Map<String, Object>> propertyMap = new HashMap<String, Map<String, Object>>();

		for (final String parentKey : collection.keySet()) {
			final Object parentValue = collection.get(parentKey);
			final Map<String, Object> fieldValues = this.transformers
					.transform(parentValue);
			propertyMap.put(parentKey, fieldValues);
		}
		return propertyMap;
	}
}
