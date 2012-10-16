package org.osehra.integration.core.exception;

public class ApplicationException extends Exception {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -7732959260838544362L;

	public ApplicationException() {
		super();
	}

	public ApplicationException(final String message) {
		super(message);
	}

	public ApplicationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(final Throwable cause) {
		super(cause);
	}
}
