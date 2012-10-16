package org.osehra.integration.http.transformer;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

/**
 * Convert from Context URI- Information to the JAXB URI information object.
 * 
 * @author Asha Amritraj
 * 
 */
public class ContextUriInfoToJaxbUriInfo implements
		Transformer<javax.ws.rs.core.UriInfo, org.osehra.integration.http.uri.UriInfo> {

	@Override
	public org.osehra.integration.http.uri.UriInfo transform(
			final javax.ws.rs.core.UriInfo contextUriInfo) throws TransformerException {
		if(NullChecker.isEmpty(contextUriInfo)) {
			return null;
		}
		try {		
			final org.osehra.integration.http.uri.UriInfo uriInfo = new org.osehra.integration.http.uri.UriInfo();
			if (NullChecker.isNotEmpty(contextUriInfo.getAbsolutePath())) {
				uriInfo.setAbsolutePath(contextUriInfo.getAbsolutePath());
			}
			if (NullChecker.isNotEmpty(contextUriInfo.getBaseUri())) {
				uriInfo.setBaseUri(contextUriInfo.getBaseUri());
			}
			uriInfo.setPath(contextUriInfo.getPath());
			if (NullChecker.isNotEmpty(contextUriInfo.getRequestUri())) {
				uriInfo.setRequestUri(contextUriInfo.getRequestUri());
			}
			// Set path parameters
			uriInfo.setPathParameters(contextUriInfo.getPathParameters());
			uriInfo.setQueryParameters(contextUriInfo.getQueryParameters());
			return uriInfo;
		} catch (Exception ex) {
			throw new TransformerException(ex);
		}
	}
}
