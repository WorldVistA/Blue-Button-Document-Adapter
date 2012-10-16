package org.osehra.das.common.transformer.bytes;

import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.validation.NullChecker;

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
	public final Object transform(final byte[] bytes)
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
