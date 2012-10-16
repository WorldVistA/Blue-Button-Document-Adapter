package org.osehra.integration.test.modifier;

public class ModifyException extends Exception {

	/**
	 * An exception thrown by implementors of the Modifier interface.
	 */
	private static final long serialVersionUID = 759651303445819416L;

	public ModifyException() {
	}

	public ModifyException(final String message) {
		super(message);
	}

	public ModifyException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ModifyException(final Throwable cause) {
		super(cause);
	}

}
