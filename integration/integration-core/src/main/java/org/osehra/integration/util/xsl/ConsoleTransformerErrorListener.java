package org.osehra.integration.util.xsl;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConsoleTransformerErrorListener implements ErrorListener {

	private static final Log LOG = LogFactory
			.getLog(ConsoleTransformerErrorListener.class);

	@Override
	public void error(final TransformerException ex)
			throws TransformerException {
		if (ConsoleTransformerErrorListener.LOG.isErrorEnabled()) {
			ConsoleTransformerErrorListener.LOG.error(ex);
		}
		throw new RuntimeException(ex);
	}

	@Override
	public void fatalError(final TransformerException ex)
			throws TransformerException {
		if (ConsoleTransformerErrorListener.LOG.isErrorEnabled()) {
			ConsoleTransformerErrorListener.LOG.error(ex);
		}
		throw new RuntimeException(ex);
	}

	@Override
	public void warning(final TransformerException ex)
			throws TransformerException {
		if (ConsoleTransformerErrorListener.LOG.isErrorEnabled()) {
			ConsoleTransformerErrorListener.LOG.error(ex);
		}
	}

}
