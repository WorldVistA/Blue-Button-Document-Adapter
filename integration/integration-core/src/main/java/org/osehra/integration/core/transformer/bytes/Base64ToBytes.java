package org.osehra.integration.core.transformer.bytes;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import org.apache.commons.codec.binary.Base64;

public class Base64ToBytes implements Transformer<String, byte[]> {

	/**
	 * decode base64 string to string
	 *
	 */

	@Override
	public byte[] transform(final String base64str) throws TransformerException {
		if (NullChecker.isEmpty(base64str)) {
			return null;
		}

		byte[] arr = Base64.decodeBase64(base64str);

		return arr;
	}
}
