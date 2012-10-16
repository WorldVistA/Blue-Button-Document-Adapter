package org.osehra.integration.jms.interceptor;

import org.osehra.integration.core.interceptor.Interceptor;
import org.osehra.integration.core.interceptor.InterceptorException;

public class JmsNoOpInterceptor implements Interceptor<Object, Object>{

	@Override
	public Object intercept(Object object) throws InterceptorException {
		return object;
	}
}
