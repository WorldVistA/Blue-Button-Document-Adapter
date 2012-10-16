package org.osehra.integration.http;

import org.osehra.integration.core.error.ExceptionHandler;
import org.osehra.integration.core.error.HandlerException;

import javax.ws.rs.core.UriInfo;

/**
 * Exception Handler for REST components.
 * 
 * @author Asha Amritraj
 * 
 */
public class ResourceExceptionHandler implements ExceptionHandler<UriInfo> {

	@Override
	public UriInfo handleException(final UriInfo t, final Exception ex)
			throws HandlerException {
		if (ResourceHandlerException.class.isInstance(ex)) {
			throw (ResourceHandlerException) ex;
		} else {
			throw new InternalServerError(ex.getMessage());
		}
	}

	@Override
	public UriInfo handleException(UriInfo t, Exception ex, final String componentName, final String componentId)
			throws HandlerException {
		return this.handleException(t, ex);
	}
	
}
