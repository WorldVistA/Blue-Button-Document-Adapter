package org.osehra.integration.core.splitter;

import org.osehra.integration.util.NullChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Default splitter. Splits a message into a list. If its an array, then adds
 * all the objects in the array to a List.
 * 
 * @author Julian Jewel
 */
public class DefaultSplitter implements Splitter<Object, List<Object>> {

	/**
	 * Split the object into a list of objects.
	 * 
	 * @param batchObject
	 *            the input object
	 * @return the split object
	 * @throws SplitterException
	 *             an error occurred during split
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final List<Object> split(final Object batchObject)
			throws SplitterException {
		if (NullChecker.isEmpty(batchObject)) {
			return null;
		}

		if (List.class.isInstance(batchObject)) {
			return (List<Object>) batchObject;
		} else if (batchObject.getClass().isArray()) {
			final List<Object> list = new ArrayList<Object>();
			final Object[] objects = (Object[]) batchObject;
			for (final Object obj : objects) {
				list.add(obj);
			}

			return list;
		} else {
			final List<Object> list = new ArrayList<Object>();
			list.add(batchObject);
			return list;
		}
	}
}
