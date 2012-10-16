package org.osehra.das.common.enricher;

import org.osehra.das.common.exception.ApplicationException;

/**
 * Exception on enrichment.
 * 
 * @author Julian Jewel
 */
public class EnricherException extends ApplicationException {

	/**
	 * This class is serializable.
	 */
	private static final long serialVersionUID = -7580470458517128003L;

	/**
	 * Default constructor.
	 */
	public EnricherException() {
		super();
	}

	/**
	 * Default constructor with message.
	 * 
	 * @param msg
	 *            the message
	 */
	public EnricherException(final String msg) {
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
	public EnricherException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default constructor with cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public EnricherException(final Throwable cause) {
		super(cause);
	}

}
