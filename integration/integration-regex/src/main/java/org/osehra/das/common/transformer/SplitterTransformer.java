package org.osehra.das.common.transformer;

import org.osehra.das.common.splitter.Splitter;
import org.osehra.das.common.splitter.SplitterException;

import org.springframework.beans.factory.annotation.Required;

public class SplitterTransformer implements Transformer<Object, Object> {

	/**
	 * @uml.property  name="splitter"
	 * @uml.associationEnd  
	 */
	Splitter<Object, Object> splitter;

	@Required
	public void setSplitter(final Splitter<Object, Object> splitter) {
		this.splitter = splitter;
	}

	@Override
	public Object transform(final Object src) throws TransformerException {
		try {
			return this.splitter.split(src);
		} catch (final SplitterException ex) {
			throw new TransformerException(ex);
		}
	}
}
