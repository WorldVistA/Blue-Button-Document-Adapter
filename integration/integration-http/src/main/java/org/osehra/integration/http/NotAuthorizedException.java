package org.osehra.integration.http;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NotAuthorizedException extends ResourceHandlerException {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -7591136971247703166L;

	public NotAuthorizedException() {
		super(Response.Status.UNAUTHORIZED);
	}

	public NotAuthorizedException(final String message) {
		super(Response.status(Response.Status.UNAUTHORIZED).entity(message)
				.type(MediaType.TEXT_PLAIN).build());
	}

	public NotAuthorizedException(final Throwable cause) {
		super(cause);
	}

}
