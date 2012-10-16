package org.osehra.das.common.composite;

import org.osehra.das.common.exception.ApplicationException;

/**
 * The exception class which is used to report exceptions in the composite
 * processor.
 * 
 * @author Julian Jewel
 */
public class CompositeProcessorException extends ApplicationException {

	/**
	 * This class is serializable.
	 */
	private static final long serialVersionUID = 7382678280794149977L;

	/**
	 * Default constructor.
	 */
	public CompositeProcessorException() {
		super();
	}

	/**
	 * Default constructor with message.
	 * 
	 * @param msg
	 *            the message
	 */
	public CompositeProcessorException(final String msg) {
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
	public CompositeProcessorException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default constructor with cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public CompositeProcessorException(final Throwable cause) {
		super(cause);
	}

}
