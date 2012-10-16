package org.osehra.das.common.test.bus;

import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;

public class StringToXML implements Transformer<String, String>{

	@Override
	public String transform(String src) throws TransformerException {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<name>" + src + "</name>";
	}
}
