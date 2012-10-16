package org.osehra.integration.test.runner;

import org.osehra.integration.core.component.Component;
import org.osehra.integration.core.component.ComponentException;

import org.springframework.beans.factory.annotation.Required;

public class ComponentTestRunner extends AbstractTestRunner {

	public Component<Object, Object> component;

	@Override
	boolean isXmlInput() {
		return false;
	}

	@Override
	protected Object transduce() {
		Object result = null;
		try {
			result = this.component.processInbound(input);
		} catch (ComponentException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * Sets the transformer that will perform the XSL transformation.
	 *
	 * @param transformer
	 *            - The transformer used to perform the transformation.
	 */
	@Required
	public void setComponent(final Component<Object, Object> component) {
		this.component = component;
	}

}
