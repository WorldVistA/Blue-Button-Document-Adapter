package org.osehra.das.common.transformer;

/**
 * Exception when transforming a source object.
 * 
 * @author Asha Amritraj
 */
public class TransformerException extends Exception {

	private static final long serialVersionUID = -6436539546308874497L;

	public TransformerException() {
		super();
	}

	public TransformerException(final String msg) {
		super(msg);
	}

	public TransformerException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public TransformerException(final Throwable cause) {
		super(cause);
	}

}
