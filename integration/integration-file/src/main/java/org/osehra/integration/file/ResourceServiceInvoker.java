package org.osehra.integration.file;

import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.util.FileUtil;
import org.osehra.integration.util.NullChecker;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

public class ResourceServiceInvoker implements ServiceInvoker<String, String> {

	/**
	 * @uml.property name="resources"
	 * @uml.associationEnd qualifier="object:java.lang.String java.lang.String"
	 */
	Map<String, String> resources;

	@Override
	public String invoke(final String object) throws ServiceInvocationException {
		if (NullChecker.isNotEmpty(object)
				&& this.resources.containsKey(object)) {
			return this.resources.get(object);
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
