package org.osehra.integration.file;

import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.util.FileUtil;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

public class StringResourceServiceInvoker implements ServiceInvoker<Object, Object> {

	Resource resource;

	@Override
	public Object invoke(Object object) throws ServiceInvocationException {
		try {
			return FileUtil.getResource(this.resource);
		} catch (IOException ex) {
			throw new ServiceInvocationException(ex);
		}
	}

	@Required
	public void setResource(Resource resource) {
		this.resource = resource;
	}
}
