package org.osehra.integration.pojo.test;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;

public class StringToXML implements Transformer<String, String> {

	@Override
	public String transform(final String src) throws TransformerException {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<name>" + src
				+ "</name>";
	}
}
