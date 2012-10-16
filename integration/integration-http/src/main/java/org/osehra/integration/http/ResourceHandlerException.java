package org.osehra.integration.http;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public abstract class ResourceHandlerException extends WebApplicationException {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 4189339421440905200L;

	public ResourceHandlerException() {
		super();
	}

	public ResourceHandlerException(final int status) {
		super(status);
	}

	public ResourceHandlerException(final Response response) {
		super(response);
	}

	public ResourceHandlerException(final Status status) {
		super(status);
	}

	public ResourceHandlerException(final Throwable cause) {
		super(cause);
	}

	public ResourceHandlerException(final Throwable cause, final int status) {
		super(cause, status);
	}

	public ResourceHandlerException(final Throwable cause,
			final Response response) {
		super(cause, response);
	}

	public ResourceHandlerException(final Throwable cause, final Status status) {
		super(cause, status);
	}
}
