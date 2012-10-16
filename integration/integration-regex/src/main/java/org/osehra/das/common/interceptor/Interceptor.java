package org.osehra.das.common.interceptor;

public interface Interceptor<E, T> {

	T intercept(E object) throws InterceptorException;
}
