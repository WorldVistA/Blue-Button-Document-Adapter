package org.osehra.integration.http.transformer;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.http.uri.UriInfo;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.JaxbUtil;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * Transform Context URI Information from the http resource to a XML document.
 * Converts to an intermediate Jaxb Object.
 * 
 * @author
 * 
 */
public class XmlToContextUriInfo implements Transformer<Document, UriInfo> {

	private JaxbUtil jaxbUtil;

	@Required
	public void setJaxbUtil(final JaxbUtil jaxbUtil) {
		this.jaxbUtil = jaxbUtil;
	}

	@Override
	public UriInfo transform(final Document document)
			throws TransformerException {
		try {
			final Object obj = this.jaxbUtil.unmarshal(document);
			Assert.assertInstance(obj, UriInfo.class);
			return (UriInfo) obj;
		} catch (final JAXBException ex) {
			throw new TransformerException(ex);
		}
	}
}
