package org.osehra.das.common.receiver;

public class MessageReceiverException extends Exception {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -1192787654091976869L;

	public MessageReceiverException() {
		super();
	}

	public MessageReceiverException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageReceiverException(String message) {
		super(message);
	}

	public MessageReceiverException(Throwable cause) {
		super(cause);
	}

}
