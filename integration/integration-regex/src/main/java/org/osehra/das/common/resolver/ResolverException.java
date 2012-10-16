package org.osehra.das.common.resolver;

/**
 * Exception when resolving based on source content.
 * 
 * @author Julian Jewel
 */
public class ResolverException extends Exception {

	/**
	 * This class is serializable.
	 */
	private static final long serialVersionUID = 1311308433064653921L;

	/**
	 * Default constructor.
	 */
	public ResolverException() {
	}

	/**
	 * Default constructor with message.
	 * 
	 * @param msg
	 *            the message
	 */
	public ResolverException(final String msg) {
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
	public ResolverException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default constructor with cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public ResolverException(final Throwable cause) {
		super(cause);
	}

}
