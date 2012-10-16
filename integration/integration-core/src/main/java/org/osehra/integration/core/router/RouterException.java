package org.osehra.integration.core.router;

import org.osehra.integration.core.exception.ApplicationException;

/**
 * An error occurred when routing the message, then the router exception is
 * thrown.
 * 
 * @author Julian Jewel
 */
public class RouterException extends ApplicationException {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 6879657739578968978L;

	/**
	 * Default constructor.
	 */
	public RouterException() {
		super();
	}

	/**
	 * Default constructor with message.
	 * 
	 * @param msg
	 *            the message
	 */
	public RouterException(final String msg) {
		super(msg);
	}

	/**
	 * Default constructor with message and cause.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public RouterException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default constructor with cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public RouterException(final Throwable cause) {
		super(cause);
	}
}
