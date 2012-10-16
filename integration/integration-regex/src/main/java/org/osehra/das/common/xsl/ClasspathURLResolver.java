package org.osehra.das.common.xsl;

import java.io.IOException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * @author Asha Amritraj
 */
@Configuration
public class ClasspathURLResolver implements URIResolver,
		ApplicationContextAware {

	/**
	 * @uml.property  name="applicationContext"
	 * @uml.associationEnd  
	 */
	private ApplicationContext applicationContext;

	protected final Source getStylesheetSource(final Resource stylesheetLocation) {
		try {
			final URL url = stylesheetLocation.getURL();
			final String urlPath = url.toString();
			final String systemId = urlPath.substring(0,
					urlPath.lastIndexOf('/') + 1);
			return new StreamSource(url.openStream(), systemId);
		} catch (final IOException ex) {
			throw new ApplicationContextException(
					"Can't load XSLT stylesheet from " + stylesheetLocation, ex);
		}
	}

	@Override
	public final Source resolve(final String href, final String base)
			throws TransformerException {
		final Resource resource = this.applicationContext
				.getResource("classpath:" + href);
		return this.getStylesheetSource(resource);
	}

	/**
	 * @param theApplicationContext
	 * @uml.property  name="applicationContext"
	 */
	@Override
	public final void setApplicationContext(
			final ApplicationContext theApplicationContext) {
		this.applicationContext = theApplicationContext;
	}

}
