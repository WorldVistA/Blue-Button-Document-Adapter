package org.osehra.das.common.bpm.engine;

import org.osehra.das.common.exception.ApplicationException;

/**
 * Exception when executing the process.
 * 
 * @author Julian Jewel
 */
public class ProcessException extends ApplicationException {

	/**
	 * This class is serializable.
	 */
	private static final long serialVersionUID = 33441397696750984L;

	/**
	 * Default constructor.
	 */
	public ProcessException() {
		super();
	}

	/**
	 * Default constructor with message.
	 * 
	 * @param msg
	 *            the message
	 */
	public ProcessException(final String msg) {
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
	public ProcessException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default constructor with cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public ProcessException(final Throwable cause) {
		super(cause);
	}
}
