package org.osehra.das.common.splitter;

import java.util.List;

public interface SizeSplitter<T> {

	public List<T> split(final T messageList, final int theSize)
			throws SplitterException;

}
