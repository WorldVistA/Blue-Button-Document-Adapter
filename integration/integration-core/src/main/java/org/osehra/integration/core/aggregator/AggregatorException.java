package org.osehra.integration.core.aggregator;

public class AggregatorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8668979043227137180L;

	public AggregatorException() {
		super();
	}

	public AggregatorException(final String message) {
		super(message);
	}

	public AggregatorException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public AggregatorException(final Throwable cause) {
		super(cause);
	}
}
