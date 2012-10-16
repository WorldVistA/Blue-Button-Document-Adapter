package org.osehra.das.common.component;

import org.osehra.das.common.exception.ApplicationException;

/**
 * Endpoint processor exception - Exception when processing a message in the
 * endpoint.
 * 
 * @author Julian Jewel
 */
public class ComponentProcessorException extends ApplicationException {

	/**
	 * This class is serializable, for EELS support.
	 */
	private static final long serialVersionUID = 431295852544915112L;

	/**
	 * Constructor.
	 */
	public ComponentProcessorException() {
		super();
	}

	/**
	 * Overriden constructor.
	 * 
	 * @param msg
	 *            - the exception message
	 */
	public ComponentProcessorException(final String msg) {
		super(msg);
	}

	/**
	 * Overridden constructor.
	 * 
	 * @param message
	 *            - The exception message
	 * @param cause
	 *            - The exception cause
	 */
	public ComponentProcessorException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Overriden constructor.
	 * 
	 * @param cause
	 *            - The failure cause
	 */
	public ComponentProcessorException(final Throwable cause) {
		super(cause);
	}

}
