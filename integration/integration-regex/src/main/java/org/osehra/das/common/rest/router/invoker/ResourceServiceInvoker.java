package org.osehra.das.common.rest.router.invoker;

import org.osehra.das.common.file.FileUtil;
import org.osehra.das.common.service.ServiceInvocationException;
import org.osehra.das.common.service.ServiceInvoker;
import org.osehra.das.common.validation.NullChecker;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

public class ResourceServiceInvoker implements ServiceInvoker<UriInfo, String> {

	/**
	 * @uml.property  name="resources"
	 * @uml.associationEnd  qualifier="url:java.lang.String java.lang.String"
	 */
	Map<String, String> resources;

	@Override
	public String invoke(final UriInfo uriInfo)
			throws ServiceInvocationException {
		if (NullChecker.isNotEmpty(uriInfo)) {
			final String url = uriInfo.getRequestUri().toString();
			if (this.resources.containsKey(url)) {
				return this.resources.get(url);
			}
		}
		return null;
	}

	@Required
	public void setResources(final Map<String, Resource> resources) {
		this.resources = new LinkedHashMap<String, String>();
		for (final Entry<String, Resource> resourceEntry : resources.entrySet()) {
			final String key = resourceEntry.getKey();
			final Resource value = resourceEntry.getValue();
			try {
				final String content = FileUtil.getResource(value);
				this.resources.put(key, content);
			} catch (final IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

}
