package org.osehra.das.common.rest;

import org.osehra.das.common.error.ExceptionHandler;
import org.osehra.das.common.error.HandlerException;

import javax.ws.rs.core.UriInfo;

/**
 * Exception Handler for REST components.
 *
 * @author Asha Amritraj
 *
 */
public class ResourceExceptionHandler implements ExceptionHandler<UriInfo> {

	@Override
	public UriInfo handleException(UriInfo t, Exception ex)
			throws HandlerException {
		if (ResourceHandlerException.class.isInstance(ex)) {
			throw (ResourceHandlerException) ex;
		} else {
			throw new InternalServerError(ex.getMessage());
		}
	}
}
