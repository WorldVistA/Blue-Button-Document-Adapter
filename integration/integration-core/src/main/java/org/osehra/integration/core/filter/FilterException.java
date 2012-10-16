package org.osehra.integration.core.filter;

public class FilterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7226139082108349439L;

	public FilterException() {
		super();
	}

	public FilterException(final String message) {
		super(message);
	}

	public FilterException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public FilterException(final Throwable cause) {
		super(cause);
	}

}
