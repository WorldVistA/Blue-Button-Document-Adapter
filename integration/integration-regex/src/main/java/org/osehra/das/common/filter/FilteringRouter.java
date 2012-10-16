package org.osehra.das.common.filter;

import org.osehra.das.common.router.Router;
import org.osehra.das.common.router.RouterException;

/**
 * A filtering router. A filter is also a router.
 * 
 * @author Julian Jewel
 * @param <T>
 *            mostly java.lang.Object
 */
public class FilteringRouter<E, T> implements Router<E, T> {

	/**
	 * The filter.
	 * @uml.property  name="filter"
	 * @uml.associationEnd  
	 */
	private Filter<E, T> filter;

	/**
	 * Route the message through the filter.
	 * 
	 * @param arg
	 *            the input message
	 * @return the result of the filter. null if the message needs to be
	 *         discarded
	 * @throws RouterException
	 *             an error occurred when filtering the message
	 */
	@Override
	public final T route(final E arg) throws RouterException {
		try {
			return this.filter.filter(arg);
		} catch (final FilterException ex) {
			throw new RouterException(ex);
		}
	}

	/**
	 * Set the filter.
	 * 
	 * @param theFilter
	 *            the filter
	 */
	public final void setFilter(final Filter<E, T> theFilter) {
		this.filter = theFilter;
	}
}
