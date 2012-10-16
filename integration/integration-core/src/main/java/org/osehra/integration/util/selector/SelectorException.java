package org.osehra.integration.util.selector;

/**
 * An exception that all implementors of Selector throws
 * 
 * @author Keith Roberts
 * 
 */
public class SelectorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8648761247627155570L;

	public SelectorException() {
	}

	public SelectorException(final String message) {
		super(message);
	}

	public SelectorException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public SelectorException(final Throwable cause) {
		super(cause);
	}

}
