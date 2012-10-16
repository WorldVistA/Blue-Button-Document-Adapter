package org.osehra.integration.core.transformer.bytes;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import java.io.IOException;

/**
 * Convert bytes to object.
 * 
 * @author Julian Jewel
 */
public class BytesToObject implements Transformer<byte[], Object> {

	/**
	 * Convert bytes to object.
	 * 
	 * @param bytes
	 *            the bytes
	 * @return the object
	 * @throws TransformerException
	 *             exception when converting bytes to object
	 */
	@Override
	public Object transform(final byte[] bytes)
			throws TransformerException {
		if (NullChecker.isEmpty(bytes)) {
			return null;
		}

		try {
			return ByteUtil.toObject(bytes);
		} catch (final ClassNotFoundException ex) {
			throw new TransformerException(ex);
		} catch (final IOException ex) {
			throw new TransformerException(ex);
		}
	}

}
