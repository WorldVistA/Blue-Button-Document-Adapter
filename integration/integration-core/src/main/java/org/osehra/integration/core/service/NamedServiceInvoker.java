package org.osehra.integration.core.service;

public interface NamedServiceInvoker<E, T> extends ServiceInvoker<E, T> {
	
	void setComponentName(String componentName);
	
	void setComponentId(String componentId);

}
