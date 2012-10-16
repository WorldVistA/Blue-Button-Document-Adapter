package org.osehra.integration.pojo.test;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;

public class StringToName implements Transformer<String, String> {

	@Override
	public String transform(final String src) throws TransformerException {
		if ("0".equals(src)) {
			return "Test0";
		} else if ("1".equals(src)) {
			return "Test1";
		}
		return "World";
	}
}
