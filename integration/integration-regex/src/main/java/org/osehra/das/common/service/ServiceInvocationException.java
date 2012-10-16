package org.osehra.das.common.service;

import org.osehra.das.common.exception.ApplicationException;

/**
 * This exception is thrown when an error occured invoking a service.
 * 
 * @author Julian Jewel
 */
public class ServiceInvocationException extends ApplicationException {

	/**
	 * This class is serializable for EELS.
	 */
	private static final long serialVersionUID = 606970102784085697L;

	/**
	 * Default constructor.
	 */
	public ServiceInvocationException() {
		super();
	}

	/**
	 * Default constructor with message.
	 * 
	 * @param msg
	 *            the message
	 */
	public ServiceInvocationException(final String msg) {
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
	public ServiceInvocationException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default constructor with cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public ServiceInvocationException(final Throwable cause) {
		super(cause);
	}
}
