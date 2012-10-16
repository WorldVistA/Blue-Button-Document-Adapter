package org.osehra.integration.core.component;

import org.osehra.integration.core.exception.ApplicationException;

/**
 * ComponentException - thrown when an error occurred on executing a component.
 * 
 * @author Julian Jewel
 */
public class ComponentException extends ApplicationException {

	/**
	 * This class is serializable.
	 */
	private static final long serialVersionUID = 1919672417499882667L;

	/**
	 * Default constructor.
	 */
	public ComponentException() {
		super();
	}

	/**
	 * Default constructor with message.
	 * 
	 * @param msg
	 *            the message
	 */
	public ComponentException(final String msg) {
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
	public ComponentException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default constructor with cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public ComponentException(final Throwable cause) {
		super(cause);
	}
}
