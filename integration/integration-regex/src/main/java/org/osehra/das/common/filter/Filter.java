package org.osehra.das.common.filter;

public interface Filter<E, T> {

	T filter(E src) throws FilterException;
}