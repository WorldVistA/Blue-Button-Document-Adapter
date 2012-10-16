package org.osehra.integration.core.filter;

import org.osehra.integration.core.router.Router;
import org.osehra.integration.core.router.RouterException;

/**
 * A batch filtering router. This enables a router configuration for the filter.
 * 
 * @author Julian Jewel
 */
public class BatchFilteringRouter extends BatchFilter implements
		Router<Object, Object> {

	/**
	 * Route the message through a filter.
	 * 
	 * @param arg
	 *            the input message
	 * @return null if the message needs to be dropped or the filtered message
	 * @throws RouterException
	 *             if an error occured when routing the message
	 */
	@Override
	public Object route(final Object arg) throws RouterException {
		try {
			return this.filter(arg);
		} catch (final FilterException ex) {
			throw new RouterException(ex);
		}
	}

}
