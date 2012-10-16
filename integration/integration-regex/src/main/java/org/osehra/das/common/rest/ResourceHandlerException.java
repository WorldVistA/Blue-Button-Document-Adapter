package org.osehra.das.common.rest;

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

	public ResourceHandlerException(int status) {
		super(status);
	}

	public ResourceHandlerException(Response response) {
		super(response);
	}

	public ResourceHandlerException(Status status) {
		super(status);
	}

	public ResourceHandlerException(Throwable cause, int status) {
		super(cause, status);
	}

	public ResourceHandlerException(Throwable cause, Response response) {
		super(cause, response);
	}

	public ResourceHandlerException(Throwable cause, Status status) {
		super(cause, status);
	}

	public ResourceHandlerException(Throwable cause) {
		super(cause);
	}
}
