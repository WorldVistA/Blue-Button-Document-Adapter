package org.osehra.das.common.rest.url.transformer;

import org.osehra.das.common.rest.url.UriInfoImpl;
import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;

import javax.ws.rs.core.UriInfo;

public class ContextUriInfoToLocalUriInfo implements
		Transformer<UriInfo, UriInfo> {

	@Override
	public UriInfo transform(final UriInfo contextUriInfo)
			throws TransformerException {
		final UriInfoImpl uriInfo = new UriInfoImpl();
		uriInfo.setAbsolutePath(contextUriInfo.getAbsolutePath());
		uriInfo.setBaseUri(contextUriInfo.getBaseUri());
		uriInfo.setPath(contextUriInfo.getPath());
		uriInfo.setPathParameters(contextUriInfo.getPathParameters());
		uriInfo.setQueryParameters(contextUriInfo.getQueryParameters());
		uriInfo.setRequestUri(contextUriInfo.getRequestUri());
		return uriInfo;
	}

}
