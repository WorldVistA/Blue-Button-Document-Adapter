package org.osehra.integration.core.validator;

import org.osehra.integration.core.exception.ApplicationException;

/**
 * Exception when validating a message.
 * 
 * @author Julian Jewel
 */
public class ValidatorException extends ApplicationException {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 6685197509928169465L;

	/**
	 * Default constructor.
	 */
	public ValidatorException() {
		super();
	}

	/**
	 * Default constructor with message.
	 * 
	 * @param msg
	 *            the message
	 */
	public ValidatorException(final String msg) {
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
	public ValidatorException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default constructor with cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public ValidatorException(final Throwable cause) {
		super(cause);
	}

}
