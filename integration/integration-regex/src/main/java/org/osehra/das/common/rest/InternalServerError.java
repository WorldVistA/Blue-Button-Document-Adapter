package org.osehra.das.common.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class InternalServerError extends ResourceHandlerException {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3457200107969056976L;

	public InternalServerError() {
		super(Response.Status.INTERNAL_SERVER_ERROR);
	}

	public InternalServerError(String message) {
		super(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(message).type(MediaType.TEXT_PLAIN).build());
	}
}
