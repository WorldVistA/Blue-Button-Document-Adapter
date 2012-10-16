package org.osehra.integration.core.filter;

public interface Filter<E, T> {

	T filter(E src) throws FilterException;
}