package org.osehra.das.common.transformer.string;

import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.validation.NullChecker;

public class ObjectToString implements Transformer<Object, String> {

	@Override
	public String transform(final Object object) throws TransformerException {
		if (NullChecker.isEmpty(object)) {
			return null;
		}

		return object.toString();
	}
}
