package org.osehra.integration.core.splitter;

import java.util.List;

public interface SizeSplitter<T> {

	public List<T> split(final T messageList, final int theSize)
			throws SplitterException;

}
