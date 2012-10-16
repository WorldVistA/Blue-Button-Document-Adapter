package org.osehra.integration.core.transformer.string;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;
import org.apache.commons.codec.binary.Base64;

/**
 * encode input string to base64 string
 * 
 */

public class StringToBase64 implements Transformer<String, String> {

	@Override
	public String transform(final String str) throws TransformerException {
		if (NullChecker.isEmpty(str)) {
			return null;
		}

		String encodedBase64String = Base64.encodeBase64String(str.getBytes()); 

		return encodedBase64String;
	}
}
