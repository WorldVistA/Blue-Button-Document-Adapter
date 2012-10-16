package org.osehra.integration.pojo.service;

import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.MethodInvoker;

/**
 * Invoke a POJO using an instance and method. The class is closely tied to
 * Spring MethodInvoker for simplicity.
 * 
 * @author Julian Jewel
 * 
 */
public class PojoServiceInvoker implements ServiceInvoker<Object, Object> {

	/**
	 * The method to execute. Uses the Spring methodInvoker
	 * 
	 * @uml.property name="methodInvoker"
	 * @uml.associationEnd
	 */
	private MethodInvoker methodInvoker;

	@Override
	public Object invoke(final Object object) throws ServiceInvocationException {

		Assert.assertNotEmpty(this.methodInvoker,
				"Method invoker cannot be empty");
		try {
			if (NullChecker.isNotEmpty(object)) {
				if (object.getClass().isArray()) {
					this.methodInvoker.setArguments((Object[]) object);
				} else {
					this.methodInvoker.setArguments(new Object[] { object });
				}
			}
			// Prepare must be called before Invoke
			this.methodInvoker.prepare();
			return this.methodInvoker.invoke();
		} catch (final ClassNotFoundException ex) {
			throw new ServiceInvocationException(ex);
		} catch (final NoSuchMethodException ex) {
			throw new ServiceInvocationException(ex);
		} catch (final InvocationTargetException ex) {
			throw new ServiceInvocationException(ex);
		} catch (final IllegalAccessException ex) {
			throw new ServiceInvocationException(ex);
		}
	}

	/**
	 * @param methodInvoker
	 * @uml.property name="methodInvoker"
	 */
	@Required
	public void setMethodInvoker(final MethodInvoker methodInvoker) {
		this.methodInvoker = methodInvoker;
	}

}
