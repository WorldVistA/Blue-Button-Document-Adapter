package org.osehra.integration.http.interceptor;

import org.osehra.integration.core.interceptor.Interceptor;
import org.osehra.integration.core.interceptor.InterceptorException;
import org.osehra.integration.http.uri.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ISIInterceptor implements Interceptor<Object, Object> {

	private static final Log LOG = LogFactory
			.getLog(ISIInterceptor.class);

	@Override
	public Object intercept(final Object object) throws InterceptorException {

		UriInfo uriInfo = (UriInfo) object;

		if(uriInfo.getPathParameters().containsKey("fileExtension")) {

				    uriInfo.setAbsolutePathExt(
					uriInfo.getAbsolutePathExt().substring(0, uriInfo.getAbsolutePathExt().lastIndexOf("/") + 1)
					+ uriInfo.getAbsolutePathExt().substring(uriInfo.getAbsolutePathExt().lastIndexOf("_") + 1,	uriInfo.getAbsolutePathExt().length())
					);
			uriInfo.setPathExt(
					uriInfo.getPathExt().substring(0, uriInfo.getPathExt().lastIndexOf("/") + 1)
					+ uriInfo.getPathExt().substring(uriInfo.getPathExt().lastIndexOf("_") + 1, uriInfo.getPathExt().length())
					);
			uriInfo.setRequestUriExt(
					uriInfo.getRequestUriExt().substring(0,	uriInfo.getRequestUriExt().lastIndexOf("/") + 1)
					+ uriInfo.getRequestUriExt().substring(uriInfo.getRequestUriExt().lastIndexOf("_") + 1, uriInfo.getRequestUriExt().length())
					);
		}

		LOG.trace("uriInfo.getAbsolutePathExt() = "
				+ uriInfo.getAbsolutePathExt());
		LOG.trace("uriInfo.getPathExt() = "
				+ uriInfo.getPathExt());
		LOG.trace("uriInfo.getRequestUriExt() = "
				+ uriInfo.getRequestUriExt());

		return uriInfo;
	}



}
