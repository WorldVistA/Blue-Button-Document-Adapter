package org.osehra.integration.core.interceptor;

public interface Interceptor<E, T> {

	T intercept(E object) throws InterceptorException;
}
