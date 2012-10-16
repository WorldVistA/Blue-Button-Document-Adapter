package org.osehra.das.common.splitter;

public interface Splitter<E, T> {
	T split(E object) throws SplitterException;
}
