package org.osehra.das.common.xsl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.springframework.context.annotation.Configuration;

/**
 * A default SAXON factory for XSL transformer factory.
 * 
 * @author Asha Amritraj
 */
@Configuration
public class XalanTransformerFactoryImpl extends
		org.apache.xalan.processor.TransformerFactoryImpl implements
		TransformerFactory {

	/**
	 * The cache of templates.
	 * @uml.property  name="templatesCache"
	 * @uml.associationEnd  qualifier="getPath:java.lang.String javax.xml.transform.Templates"
	 */
	private final Hashtable<String, Templates> templatesCache = new Hashtable<String, Templates>();

	@Override
	public final Templates newTemplates(final URL url)
			throws TransformerConfigurationException {
		if (this.templatesCache.containsKey(url.getPath())) {
			return this.templatesCache.get(url.getPath());
		}
		StreamSource source = null;
		InputStream is = null;
		try {
			is = url.openStream();
			source = new StreamSource(is);
		} catch (final IOException ex) {
			throw new TransformerConfigurationException(ex);
		}
		final Templates templates = super.newTemplates(source);
		try {
			is.close();
		} catch (final IOException ex) {
			throw new TransformerConfigurationException(ex);
		}
		if (templates == null) {
			throw new RuntimeException(
					"Failed to create templates for stylesheet "
							+ url.getPath());
		}

		this.templatesCache.put(url.getPath(), templates);
		return templates;
	}

	@Override
	public final Transformer newTransformer()
			throws TransformerConfigurationException {
		return super.newTransformer();
	}

	@Override
	public final void setURIResolver(final ClasspathURLResolver resolver) {
		super.setURIResolver(resolver);
	}
}
