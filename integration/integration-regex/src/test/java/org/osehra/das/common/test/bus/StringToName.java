package org.osehra.das.common.test.bus;

import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;

public class StringToName implements Transformer<String, String> {

	@Override
	public String transform(String src) throws TransformerException {
		if ("0".equals(src)) {
			return "Test0";
		} else if ("1".equals(src)) {
			return "Test1";
		}
		return "World";
	}
}
