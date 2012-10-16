package org.osehra.das.common.xsl;

import java.net.URL;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

/**
 * @author Asha Amritraj
 */

public interface TransformerFactory {
	Templates newTemplates(URL url) throws TransformerConfigurationException;

	Transformer newTransformer() throws TransformerConfigurationException;

	void setURIResolver(final ClasspathURLResolver resolver);

}
