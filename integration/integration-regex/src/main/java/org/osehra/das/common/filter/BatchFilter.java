package org.osehra.das.common.filter;

import org.osehra.das.common.validation.NullChecker;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * Batch filter. Passes the input through a list of filters.
 * 
 * @author Julian Jewel
 */
public class BatchFilter implements Filter<Object, Object> {

	/**
	 * List of filters.
	 * @uml.property  name="filters"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.osehra.das.common.filter.Filter"
	 */
	private List<Filter<Object, Object>> filters;

	/**
	 * Pass the message through multiple filters.
	 * 
	 * @param object
	 *            the input message
	 * @return null if the object needs to be filtered or the object itself
	 * @throws FilterException
	 *             exception occured on filtering the message
	 */
	@Override
	public final Object filter(final Object object) throws FilterException {
		if (NullChecker.isEmpty(object)) {
			return null;
		}

		Object resultObject = object;
		for (final Filter<Object, Object> filter : this.filters) {
			resultObject = filter.filter(resultObject);
			if (NullChecker.isEmpty(resultObject)) {
				return resultObject;
			}
		}
		return resultObject;
	}

	/**
	 * Set the list of filters.
	 * 
	 * @param theFilters
	 *            the list of filters
	 */
	@Required
	public final void setFilters(final List<Filter<Object, Object>> theFilters) {
		this.filters = theFilters;
	}
}
