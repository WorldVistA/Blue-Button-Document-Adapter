package org.osehra.das.common.file;

import org.osehra.das.common.service.ServiceInvocationException;
import org.osehra.das.common.service.ServiceInvoker;
import org.osehra.das.common.validation.NullChecker;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

public class ResourceValueServiceInvoker implements
		ServiceInvoker<String, String> {

	/**
	 * @uml.property  name="resources"
	 * @uml.associationEnd  qualifier="object:java.lang.String java.lang.String"
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
