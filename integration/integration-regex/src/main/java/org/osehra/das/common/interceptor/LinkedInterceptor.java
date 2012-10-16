package org.osehra.das.common.interceptor;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

public class LinkedInterceptor implements Interceptor<Object, Object> {

	/**
	 * @uml.property  name="interceptors"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.osehra.das.common.interceptor.Interceptor"
	 */
	List<Interceptor<Object, Object>> interceptors;

	@Override
	public Object intercept(final Object object) throws InterceptorException {
		Object result = object;
		for (final Interceptor<Object, Object> interceptor : this.interceptors) {
			result = interceptor.intercept(result);
		}
		return result;
	}

	@Required
	public void setInterceptors(
			final List<Interceptor<Object, Object>> interceptors) {
		this.interceptors = interceptors;
	}

}
