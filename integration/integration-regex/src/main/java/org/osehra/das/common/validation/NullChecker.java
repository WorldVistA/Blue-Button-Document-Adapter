package org.osehra.das.common.validation;

import java.util.Collection;

/**
 * Validation Checks.
 * 
 * @author Asha Amritraj
 */
public class NullChecker {

	public static boolean hasEmpty(final Object... objects) {
		if (objects == null) {
			return true;
		}

		for (final Object object : objects) {
			if (NullChecker.isEmpty(object)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmpty(final Object object) {
		if (object == null) {
			return true;
		} else if (String.class.isInstance(object)) {
			return ((String) object).length() <= 0;
		} else if (StringBuffer.class.isInstance(object)) {
			return ((StringBuffer) object).length() <= 0;
		} else if (Collection.class.isInstance(object)) {
			return ((Collection<?>) object).isEmpty();
		}
		return false;
	}

	public static boolean isInstance(final Object object, final Class<?> clazz) {
		Assert.assertNotEmpty(clazz, "Class cannot be empty!");
		return clazz.isInstance(object);
	}

	public static boolean isNotEmpty(final Object object) {
		return !NullChecker.isEmpty(object);
	}

	/**
	 * Checks whether the object is null or empty.
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isNullOrEmpty(final Object object) {
		return NullChecker.isEmpty(object);
	}

	protected NullChecker() {
	}
}
