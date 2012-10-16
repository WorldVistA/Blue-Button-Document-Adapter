package org.osehra.integration.core.transformer.string;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import org.apache.commons.codec.binary.Base64;

public class Base64ToString implements Transformer<String, String> {

	/**
	 * decode base64 string to string
	 * 
	 */
	
	@Override
	public String transform(final String base64str) throws TransformerException {
		if (NullChecker.isEmpty(base64str)) {
			return null;
		}
		
		byte[] arr = Base64.decodeBase64(base64str);
		String decodedString = new String(arr);

		return decodedString;
	}
}
