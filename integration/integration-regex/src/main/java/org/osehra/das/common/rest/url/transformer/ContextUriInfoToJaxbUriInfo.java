package org.osehra.das.common.rest.url.transformer;

import org.osehra.das.common.rest.url.MultiValuedMap;
import org.osehra.das.common.rest.url.MultiValuedMap.Entry;
import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.validation.NullChecker;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

public class ContextUriInfoToJaxbUriInfo implements
		Transformer<UriInfo, org.osehra.das.common.rest.url.UriInfo> {

	@Override
	public org.osehra.das.common.rest.url.UriInfo transform(final UriInfo contextUriInfo)
			throws TransformerException {
		org.osehra.das.common.rest.url.UriInfo uriInfo = new org.osehra.das.common.rest.url.UriInfo();
		uriInfo.setAbsolutePath(contextUriInfo.getAbsolutePath().toString());
		uriInfo.setBaseUri(contextUriInfo.getBaseUri().toString());
		uriInfo.setPath(contextUriInfo.getPath());
		uriInfo.setRequestUri(contextUriInfo.getRequestUri().toString());
		/*MultiValuedMap multiValuedPathParametersMap = new MultiValuedMap();
		MultivaluedMap multiValuedContextPathParametersMap = contextUriInfo
				.getPathParameters();
		if (NullChecker.isNotEmpty(multiValuedContextPathParametersMap)) {
			for (java.util.Map.Entry entry : multiValuedContextPathParametersMap
					.entrySet()) {
				Entry pathEntry = new Entry();
				pathEntry.setKey(entry.getKey());
				List<String> pathValues = pathEntry.getValue();
				for (String pathValue : pathValues) {
					pathEntry.getValue().add(pathValue);
				}
				multiValuedPathParametersMap.getEntry().add(pathEntry);
			}
		}

		MultiValuedMap multiValuedQueryParametersMap = new MultiValuedMap();
		MultivaluedMap<String, String> multiValuedContextQueryParametersMap = contextUriInfo
				.getQueryParameters();
		if (NullChecker.isNotEmpty(multiValuedContextQueryParametersMap)) {
			for (java.util.Map.Entry<String, List<String>> entry : multiValuedContextQueryParametersMap
					.entrySet()) {
				Entry queryEntry = new Entry();
				queryEntry.setKey(entry.getKey());
				List<String> queryValues = queryEntry.getValue();
				for (String queryValue : queryValues) {
					queryEntry.getValue().add(queryValue);
				}
				multiValuedQueryParametersMap.getEntry().add(queryEntry);
			}
		}*/
		return uriInfo;
	}
}
