package org.osehra.integration.core.transformer.bytes;

import org.osehra.integration.util.NullChecker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Byte utilities.
 * 
 * @author Julian Jewel
 */
public class ByteUtil {

	/**
	 * Convert object to bytes.
	 * 
	 * @param o
	 *            the object
	 * @return the bytes
	 * @throws IOException
	 *             exception occured on conversion
	 */
	public static byte[] fromObject(final Object o) throws IOException {
		if (NullChecker.isNotEmpty(o) && Serializable.class.isInstance(o)) {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(o);
			out.close();
			return bos.toByteArray();
		}
		throw new IOException("Object is null or not serializable!");
	}

	/**
	 * Convert bytes to object. The class type has to be in the classpath.
	 * 
	 * @param bytes
	 *            the bytes
	 * @return the object
	 * @throws IOException
	 *             exception when converting
	 * @throws ClassNotFoundException
	 *             if the class is not found
	 */
	public static Object toObject(final byte[] bytes) throws IOException,
			ClassNotFoundException {
		if (bytes != null) {
			final ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bytes));
			final Object obj = in.readObject();
			in.close();
			return obj;
		}
		throw new IOException("bytes array is null!");
	}

	/**
	 * Default constructor for utility class.
	 */
	protected ByteUtil() {
	}

}
