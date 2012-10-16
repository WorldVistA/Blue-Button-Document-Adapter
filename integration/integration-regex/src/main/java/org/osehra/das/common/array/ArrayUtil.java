package org.osehra.das.common.array;

import org.osehra.das.common.validation.NullChecker;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Array utilities.
 * 
 * @author Julian Jewel
 */
public class ArrayUtil {

	/**
	 * Concatenate the String elements of an array.
	 * 
	 * @param concatArray
	 *            concat array of strings
	 * @return the concatenated string
	 */
	public static String concat(final List<String> concatArray) {
		if (NullChecker.isEmpty(concatArray)) {
			return "";
		}
		final StringBuffer strBuffer = new StringBuffer();
		for (final String str : concatArray) {
			strBuffer.append(str);
		}
		return strBuffer.toString();
	}

	/**
	 * Remove the empty elements from the list.
	 * 
	 * @param c
	 *            the list
	 * @return cleaned up list
	 */
	public static List<?> removeEmpty(final List<?> c) {
		if (NullChecker.isNotEmpty(c)) {
			final ArrayList<Object> cleanList = new ArrayList<Object>();
			for (final Object resultElement : (List<?>) c) {
				if (List.class.isInstance(resultElement)) {
					final List<?> elements = ArrayUtil.removeEmpty(c);
					if (NullChecker.isNotEmpty(elements)) {
						cleanList.addAll(elements);
					}
				} else if (NullChecker.isNotEmpty(resultElement)) {
					cleanList.add(resultElement);
				}
			}
			return cleanList;
		} else {
			return c;
		}
	}

	/**
	 * Add to an array and if the array is empty then create a new array.
	 * 
	 * @param <T>
	 *            the type
	 * @param c
	 *            the list
	 * @param e
	 *            the element
	 * @return the list with the element added
	 */
	public static <T extends Object> List<T> safeAdd(final List<T> c, final T e) {
		List<T> arrayList = c;
		if (NullChecker.isEmpty(c)) {
			arrayList = new ArrayList<T>();
		}
		arrayList.add(e);
		return arrayList;
	}

	/**
	 * Get the size of the array.
	 * 
	 * @param stringArray
	 *            the string array
	 * @return The size of the strings in the array
	 */
	public static int sizeOf(final List<String> stringArray) {
		int arraySize = 0;
		if (NullChecker.isEmpty(stringArray)) {
			return arraySize;
		}
		for (final String str : stringArray) {
			arraySize += str.length();
		}
		return arraySize;
	}

	/**
	 * UNSAFE - Use with caution! 1. The collection contains one or more
	 * elements. 2. The first element is exactly a T an not a subclass thereof.
	 * 
	 * @param <T>
	 *            the type
	 * @param <E>
	 *            the collection type
	 * @param c
	 *            the collection
	 * @return the array
	 */
	@SuppressWarnings("all")
	public static <T, E extends T> T[] toArray(final Collection<E> c) {
		if ((c != null) && (c.size() > 0)) {
			return c.toArray((T[]) Array.newInstance(c.iterator().next()
					.getClass(), c.size()));
		}
		return null;
	}

	/**
	 * UNSAFE - Use with caution! 1. The collection contains one or more
	 * elements. 2. The first element is exactly a T an not a subclass thereof.
	 * 
	 * @param <T>
	 *            the type
	 * @param <E>
	 *            the collection type
	 * @param c
	 *            the collection
	 * @param componentType
	 *            the type of array to construct
	 * @return the array
	 */
	@SuppressWarnings("all")
	public static <T, E extends T> T[] toArray(final Collection<E> c,
			final Class<T> componentType) {
		if ((c != null) && (c.size() > 0)) {
			return c.toArray((T[]) Array.newInstance(componentType, c.size()));
		}
		return null;
	}

	/**
	 * Default protected constructor.
	 */
	protected ArrayUtil() {
	}

}
