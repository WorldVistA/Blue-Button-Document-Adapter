package org.osehra.integration.bpm.engine;

import org.osehra.integration.core.exception.ApplicationException;

/**
 * The activity exception. Used mostly in the BPM to throw checked exceptions.
 * 
 * @author Julian Jewel
 */
public class ActivityException extends ApplicationException {
	/**
	 * This class is serializable.
	 */
	private static final long serialVersionUID = -2632146759017111048L;

	/**
	 * Default constructor.
	 */
	public ActivityException() {
		super();
	}

	/**
	 * Default constructor with message.
	 * 
	 * @param msg
	 *            the message
	 */
	public ActivityException(final String msg) {
		super(msg);
	}

	/**
	 * Default constructor with the message and cause.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public ActivityException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default constructor with cause.
	 * 
	 * @param cause
	 *            the thorwable cause
	 */
	public ActivityException(final Throwable cause) {
		super(cause);
	}

}
