package org.osehra.integration.core.transformer.bytes;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import java.io.IOException;

/**
 * Convert object to bytes.
 * 
 * @author Julian Jewel
 */
public class ObjectToBytes implements Transformer<Object, byte[]> {
	/**
	 * Convert object to bytes.
	 * 
	 * @param obj
	 *            the object
	 * @return the bytes
	 * @throws TransformerException
	 *             exception when transforming the object
	 */
	@Override
	public byte[] transform(final Object obj) throws TransformerException {
		if (NullChecker.isEmpty(obj)) {
			return null;
		}

		try {
			return ByteUtil.fromObject(obj);
		} catch (final IOException ex) {
			throw new TransformerException(ex);
		}
	}
}
