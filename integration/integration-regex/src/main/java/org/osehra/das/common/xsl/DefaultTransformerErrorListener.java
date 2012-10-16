package org.osehra.das.common.xsl;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @author Asha Amritraj
 */
@Configuration
public class DefaultTransformerErrorListener implements ErrorListener {

	private static final Log LOG = LogFactory
			.getLog(DefaultTransformerErrorListener.class);

	@Override
	public final void error(final TransformerException ex)
			throws TransformerException {
		if (DefaultTransformerErrorListener.LOG.isErrorEnabled()) {
			DefaultTransformerErrorListener.LOG.error(ex);
		}
		if (RuntimeException.class.isInstance(ex.getException())) {
			throw (RuntimeException) ex.getException();
		} else {
			throw ex;
		}
	}

	@Override
	public final void fatalError(final TransformerException ex)
			throws TransformerException {
		if (DefaultTransformerErrorListener.LOG.isErrorEnabled()) {
			DefaultTransformerErrorListener.LOG.error(ex);
		}
		if (RuntimeException.class.isInstance(ex.getException())) {
			throw (RuntimeException) ex.getException();
		} else {
			throw ex;
		}
	}

	@Override
	public final void warning(final TransformerException ex)
			throws TransformerException {
		if (DefaultTransformerErrorListener.LOG.isWarnEnabled()) {
			DefaultTransformerErrorListener.LOG.warn(ex);
		}
	}

}
