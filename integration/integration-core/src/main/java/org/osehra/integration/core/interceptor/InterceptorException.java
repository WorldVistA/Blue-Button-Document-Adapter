package org.osehra.integration.core.interceptor;

public class InterceptorException extends Exception {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -1178113005405786743L;

	public InterceptorException() {
		super();
	}

	public InterceptorException(final String message) {
		super(message);
	}

	public InterceptorException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public InterceptorException(final Throwable cause) {
		super(cause);
	}
}
