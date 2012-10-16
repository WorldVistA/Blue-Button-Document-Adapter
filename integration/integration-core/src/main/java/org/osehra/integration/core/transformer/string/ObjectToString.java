package org.osehra.integration.core.transformer.string;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

public class ObjectToString implements Transformer<Object, String> {

	@Override
	public String transform(final Object object) throws TransformerException {
		if (NullChecker.isEmpty(object)) {
			return null;
		}

		return object.toString();
	}
}
