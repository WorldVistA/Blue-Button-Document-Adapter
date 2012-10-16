package org.osehra.das.common.error;

import org.osehra.das.common.exception.ApplicationException;

/**
 * Exception when handling the component exception.
 * 
 * @author Julian Jewel
 */
public class HandlerException extends ApplicationException {
	/**
	 * This class is serializable.
	 */
	private static final long serialVersionUID = 7851402190796989130L;

	/**
	 * Default constructor.
	 */
	public HandlerException() {
		super();
	}

	/**
	 * Default constructor with message.
	 * 
	 * @param msg
	 *            the message
	 */
	public HandlerException(final String msg) {
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
	public HandlerException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default constructor with cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public HandlerException(final Throwable cause) {
		super(cause);
	}
}
