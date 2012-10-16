package org.osehra.das.common.file;

import org.osehra.das.common.regex.RegExUtil;
import org.osehra.das.common.service.ServiceInvocationException;
import org.osehra.das.common.service.ServiceInvoker;
import org.osehra.das.common.validation.NullChecker;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.ws.rs.core.UriInfo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

public class ClasspathResourceServiceInvoker implements
		ServiceInvoker<UriInfo, byte[]>, ApplicationContextAware {

	/**
	 * @uml.property  name="baseResourceLocation"
	 */
	String baseResourceLocation;
	/**
	 * @uml.property  name="ctx"
	 * @uml.associationEnd  
	 */
	ApplicationContext ctx;
	/**
	 * @uml.property  name="resourceKeyExpression"
	 */
	Pattern resourceKeyExpression;

	@Override
	public byte[] invoke(final UriInfo uriInfo)
			throws ServiceInvocationException {
		if (NullChecker.isNotEmpty(uriInfo)) {
			final String url = uriInfo.getRequestUri().toString();
			if (NullChecker.isNotEmpty(url)) {
				final String match = RegExUtil.getFirstMatchValue(url,
						this.resourceKeyExpression);
				try {
					final Resource resource = this.ctx
							.getResource(this.baseResourceLocation + match);
					return FileUtil.getResourceAsBytes(resource);
				} catch (final IOException ex) {
					throw new ServiceInvocationException(ex);
				}
			}
		}
		return null;
	}

	@Override
	public void setApplicationContext(final ApplicationContext ctx)
			throws BeansException {
		this.ctx = ctx;
	}

	/**
	 * @param baseResourceLocation
	 * @uml.property  name="baseResourceLocation"
	 */
	@Required
	public void setBaseResourceLocation(final String baseResourceLocation) {
		this.baseResourceLocation = baseResourceLocation;
	}

	/**
	 * @param resourceKeyExpression
	 * @uml.property  name="resourceKeyExpression"
	 */
	@Required
	public void setResourceKeyExpression(final Pattern resourceKeyExpression) {
		this.resourceKeyExpression = resourceKeyExpression;
	}

}
