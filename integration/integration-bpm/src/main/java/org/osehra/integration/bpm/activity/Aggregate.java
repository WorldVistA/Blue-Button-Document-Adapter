package org.osehra.integration.bpm.activity;

import org.osehra.integration.bpm.engine.ActivityException;
import org.osehra.integration.bpm.engine.ProcessContext;
import org.osehra.integration.core.splitter.Splitter;
import org.osehra.integration.core.splitter.SplitterException;
import org.osehra.integration.util.ArrayUtil;
import org.osehra.integration.util.NullChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate the input. Split the results if needed.
 * 
 * @author Julian Jewel
 */
public class Aggregate extends TransitionImpl {

	/**
	 * Flatten arrays. If there are multiple arrays or array within arrays then
	 * flatten all of the documents into one array.
	 * 
	 * @uml.property name="flattenArrays"
	 */
	private boolean flattenArrays = true;

	/**
	 * Remove empty elements would remove all the empty arrays.
	 * 
	 * @uml.property name="removeEmpty"
	 */
	private boolean removeEmpty = true;

	/**
	 * Splitter is used if the aggregated result has to be split again or each
	 * document in the array is split and aggregated.
	 * 
	 * @uml.property name="splitter"
	 * @uml.associationEnd
	 */
	private Splitter<Object, Object> splitter;

	/**
	 * Aggregate the input. The input could be multiple arrays.
	 * 
	 * @param source
	 *            the input XML document or array of XML documents
	 * @return the aggregated document list
	 * @throws ActivityException
	 *             if an error occured in aggregation
	 */
	protected final Object aggregate(final Object source)
			throws ActivityException {
		// If flatten array and list is the input
		if (this.flattenArrays && List.class.isInstance(source)) {
			final ArrayList<Object> arrayList = new ArrayList<Object>();
			final List<?> arr = (List<?>) source;
			for (final Object object : arr) {
				// Flatten array
				this.flattenArray(arrayList, object);
			}
			// Return the flattened array
			return arrayList;
		} else {
			// If there is a splitter, then split the XML document
			if (NullChecker.isNotEmpty(this.splitter)) {
				try {
					return this.splitter.split(source);
				} catch (final SplitterException ex) {
					throw new ActivityException(ex);
				}
			} else {
				return source;
			}
		}
	}

	/**
	 * Execute the aggregate activity.
	 * 
	 * @param context
	 *            the process context
	 * @param source
	 *            the input XML or array of XML documents
	 * @return Return the aggregated document
	 * @throws ActivityException
	 *             if an error occurred on aggregation
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected final Object executeAction(final ProcessContext context,
			final Object source) throws ActivityException {
		if (NullChecker.isEmpty(source)) {
			return source;
		}
		// If there are arrays of sources - then aggregate each one of them
		// For example - input = x, y, z - then aggregate the values of
		// x, y and z. If y is an array, then flatten the array.
		Object result;
		// Aggregate the input primitive array
		if (source.getClass().isArray()) {
			final Object[] sources = (Object[]) source;
			final List<Object> arrayList = new ArrayList<Object>();
			for (final Object obj : sources) {
				// Aggregate
				final Object aggregatedObject = this.aggregate(obj);
				if (List.class.isInstance(aggregatedObject)) {
					// Add individual elements into the arrayList
					arrayList.addAll((ArrayList<Object>) aggregatedObject);
				} else {
					// Single document, then add it to the arrayList
					arrayList.add(aggregatedObject);
				}
			}
			result = arrayList;
		} else {
			// Aggregate the input ArrayList or Document
			result = this.aggregate(source);
		}

		// Remove empty if flag is present
		if (this.removeEmpty && List.class.isInstance(result)) {
			return ArrayUtil.removeEmpty((List<?>) result);
		}
		return result;
	}

	/**
	 * Flatten the array. Split the individual elements if splitter is present.
	 * 
	 * @param sources
	 *            the list to add the elements
	 * @param source
	 *            the input document or array
	 * @throws ActivityException
	 *             if an error occurred in flattening the array
	 */
	protected final void flattenArray(final List<Object> sources,
			final Object source) throws ActivityException {
		// If the object is a list, then flatten the list
		if (List.class.isInstance(source)) {
			final List<?> arr = (List<?>) source;
			for (final Object obj : arr) {
				// If the elements within the array is a list, flatten that as
				// well
				this.flattenArray(sources, obj);
			}
		} else {
			// If its a single document, then split it and add the result to the
			// final array (sources)
			if (NullChecker.isNotEmpty(this.splitter)) {
				try {
					final Object array = this.splitter.split(source);
					if (List.class.isInstance(array)) {
						sources.addAll((List<?>) array);
					} else {
						sources.add(array);
					}
				} catch (final SplitterException ex) {
					throw new ActivityException(ex);
				}
			} else {
				// Just add the result to the final array
				sources.add(source);
			}
		}
	}

	/**
	 * Set the boolean flatten arrays. If true, then the arrays will be
	 * flattened. False, would just aggregate all inputs into a list.
	 * 
	 * @param theFlattenArrays
	 *            boolean true of false
	 * @uml.property name="flattenArrays"
	 */
	public void setFlattenArrays(final boolean theFlattenArrays) {
		this.flattenArrays = theFlattenArrays;
	}

	/**
	 * Set the boolean remove empty flag.
	 * 
	 * @param theRemoveEmpty
	 *            true if the empty elements have to be removed in an array.
	 * @uml.property name="removeEmpty"
	 */
	public void setRemoveEmpty(final boolean theRemoveEmpty) {
		this.removeEmpty = theRemoveEmpty;
	}

	/**
	 * Set the splitter.
	 * 
	 * @param theSplitter
	 *            the splitter to split the aggregated array if needed
	 */
	public void setSplitter(final Splitter<Object, Object> theSplitter) {
		this.splitter = theSplitter;
	}
}
