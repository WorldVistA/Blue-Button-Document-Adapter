package org.osehra.das.common.xpath;

public class XPathException extends Exception {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 2116995904051630851L;

	public XPathException() {
		super();
	}

	public XPathException(final String message) {
		super(message);
	}

	public XPathException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public XPathException(final Throwable cause) {
		super(cause);
	}

}
