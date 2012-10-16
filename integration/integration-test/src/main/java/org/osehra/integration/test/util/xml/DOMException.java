package org.osehra.integration.test.util.xml;

/**
 * An application exception throws from methods that performs DOM operations.
 * 
 * @author Keith Roberts
 * 
 */
public class DOMException extends RuntimeException {

	/**
	 * An exception class to identify DOM exceptions.
	 * 
	 * @author Julian Jewel
	 */
	private static final long serialVersionUID = 2840239878037537978L;

	/**
	 * The overloaded constructor.
	 */
	public DOMException() {
	}

	/**
	 * The overloaded constructor.
	 * 
	 * @param msg
	 *            - The exception Message
	 */
	public DOMException(final String msg) {
		super(msg);
	}

	/**
	 * The overloaded constructor.
	 * 
	 * @param msg
	 *            - The exception Message
	 * @param cause
	 *            - The throwable cause
	 */
	public DOMException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	/**
	 * The overloaded constructor.
	 * 
	 * @param cause
	 *            - The throwable cause
	 */
	public DOMException(final Throwable cause) {
		super(cause);
	}

}
