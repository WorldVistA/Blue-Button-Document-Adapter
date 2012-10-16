package org.osehra.integration.core.receiver;

public class MessageReceiverException extends Exception {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -1192787654091976869L;

	public MessageReceiverException() {
		super();
	}

	public MessageReceiverException(final String message) {
		super(message);
	}

	public MessageReceiverException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MessageReceiverException(final Throwable cause) {
		super(cause);
	}

}
