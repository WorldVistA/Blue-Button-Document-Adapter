package org.osehra.das.common.validation;

/**
 * Assert convenience class.
 * 
 * @author Julian Jewel
 */
public class Assert {

	/**
	 * Throw an exception if the object's array size is less than size.
	 * 
	 * @param object
	 *            the object to check
	 * @param size
	 *            the size to check against
	 * @param message
	 *            the exception message
	 */
	public static final void assertArraySize(final Object object,
			final int size, final String message) {
		if (object == null) {
			throw new RuntimeException(message);
		}
		if (!object.getClass().isArray()) {
			throw new RuntimeException(message);
		}
		if (((Object[]) object).length < size) {
			throw new RuntimeException(message);
		}
	}

	/**
	 * Throw an exception if not empty.
	 * 
	 * @param object
	 *            the object
	 * @param message
	 *            the exception message
	 */
	public static final void assertEmpty(final Object object,
			final String message) {
		if (NullChecker.isNotEmpty(object)) {
			throw new RuntimeException(message);
		}
	}

	/**
	 * Throws an exception even if one object is not empty.
	 * 
	 * @param message
	 *            the exception message
	 * @param objects
	 *            the objects
	 */
	public static final void assertEmptyAnd(final String message,
			final Object... objects) {
		for (final Object object : objects) {
			if (NullChecker.isNotEmpty(object)) {
				throw new RuntimeException(message);
			}
		}
	}

	/**
	 * Throws an exception if one of the element is not empty.
	 * 
	 * @param message
	 *            the exception message
	 * @param objects
	 *            the objects
	 */
	public static final void assertEmptyOr(final String message,
			final Object... objects) {
		for (final Object object : objects) {
			if (NullChecker.isEmpty(object)) {
				return;
			}
		}
		throw new RuntimeException(message);
	}

	/**
	 * Throw an exception if value is true.
	 * 
	 * @param value
	 *            the boolean value
	 * @param message
	 *            the exception message
	 */
	public static final void assertFalse(final boolean value,
			final String message) {
		if (value) {
			throw new RuntimeException(message);
		}
	}

	/**
	 * Throws an exception if the object is not of type clazz.
	 * 
	 * @param object
	 *            the object
	 * @param clazz
	 *            the class to compared
	 */
	public static final void assertInstance(final Object object,
			final Class<?> clazz) {
		if (!clazz.isInstance(object)) {
			throw new RuntimeException("Object has to be of type "
					+ clazz.getName());
		}
	}

	/**
	 * Throws an exception if the object is not of type clazz.
	 * 
	 * @param object
	 *            the object
	 * @param clazz
	 *            the class to check against
	 * @param message
	 *            the exception message
	 */
	public static final void assertInstance(final Object object,
			final Class<?> clazz, final String message) {
		if (!clazz.isInstance(object)) {
			throw new RuntimeException(message);
		}
	}

	/**
	 * Throws an exception if atleast the object is atleast one of the class
	 * types.
	 * 
	 * @param object
	 *            the object
	 * @param clazzes
	 *            the class types to compare against.
	 */
	public static final void assertInstanceOr(final Object object,
			final Class<?>... clazzes) {
		if (clazzes == null) {
			throw new RuntimeException("Classes cannot be null!");
		}
		for (final Class<?> clazz : clazzes) {
			if (clazz.isInstance(object)) {
				return;
			}
		}
		final StringBuffer buffer = new StringBuffer();
		for (final Class<?> clazz : clazzes) {
			buffer.append("," + clazz.getName());
		}
		throw new RuntimeException("Object has to be of type "
				+ buffer.toString());
	}

	/**
	 * Throw an exception if one of the elements is empty.
	 * 
	 * @param message
	 *            the message
	 * @param objects
	 *            the array of objects
	 */
	public static final void assertNonEmptyAnd(final String message,
			final Object... objects) {
		for (final Object object : objects) {
			Assert.assertNotEmpty(object, message);
		}
	}

	/**
	 * Throw an exception if its an empty array.
	 * 
	 * @param object
	 *            the object to check
	 * @param message
	 *            the exception message
	 */
	public static final void assertNonEmptyArray(final Object object,
			final String message) {
		if (object == null) {
			throw new RuntimeException(message);
		}
		if (!object.getClass().isArray()) {
			throw new RuntimeException(message);
		}
		for (final Object obj : (Object[]) object) {
			if (NullChecker.isNotEmpty(obj)) {
				return;
			}
		}
		throw new RuntimeException(message);
	}

	/**
	 * Throw an exception if atleast all elements are empty.
	 * 
	 * @param message
	 *            the exception message
	 * @param objects
	 *            the array of objects
	 */
	public static final void assertNonEmptyOr(final String message,
			final Object... objects) {
		for (final Object object : objects) {
			if (NullChecker.isNotEmpty(object)) {
				return;
			}
		}
		throw new RuntimeException(message);
	}

	/**
	 * Throws an exception if empty!
	 * 
	 * @param object
	 *            the object
	 * @param message
	 *            the message
	 */
	public static final void assertNotEmpty(final Object object,
			final String message) {
		if (NullChecker.isEmpty(object)) {
			throw new RuntimeException(message);
		}
	}

	/**
	 * Throw an exception if value is false.
	 * 
	 * @param value
	 *            the boolean value
	 * @param message
	 *            the exception message
	 */
	public static final void assertTrue(final boolean value,
			final String message) {
		if (!value) {
			throw new RuntimeException(message);
		}
	}

	/**
	 * Default constructor.
	 */
	protected Assert() {
	}

}
