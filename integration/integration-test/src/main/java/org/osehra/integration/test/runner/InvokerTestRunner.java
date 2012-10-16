package org.osehra.integration.test.runner;

import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;

import org.springframework.beans.factory.annotation.Required;

public class InvokerTestRunner extends AbstractTestRunner {

	public ServiceInvoker<Object, Object> invoker;

	@Override
	boolean isXmlInput() {
		return false;
	}

	@Override
	protected Object transduce() {
		Object result;
		try {
			result = this.invoker.invoke(input);
		} catch (final ServiceInvocationException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * Sets the invoker
	 *
	 * @param invoker
	 *            - The invoker.
	 */
	@Required
	public void setInvoker(final ServiceInvoker<Object, Object> invoker) {
		this.invoker = invoker;
	}

}
