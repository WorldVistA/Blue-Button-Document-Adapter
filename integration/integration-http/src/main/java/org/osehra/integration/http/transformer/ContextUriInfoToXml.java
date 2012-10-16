package org.osehra.integration.http.transformer;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.JaxbUtil;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * Transform Context URI Information from the http resource to a XML document.
 * Converts to an intermediate Jaxb Object.
 * 
 * @author Asha Amritraj
 * 
 */
public class ContextUriInfoToXml implements Transformer<UriInfo, Document> {

	private JaxbUtil jaxbUtil;
	private Transformer<UriInfo, org.osehra.integration.http.uri.UriInfo> uriInfoToJaxbUriInfo;

	@Required
	public void setJaxbUtil(final JaxbUtil jaxbUtil) {
		this.jaxbUtil = jaxbUtil;
	}

	@Required
	public void setUriInfoToJaxbUriInfo(
			final Transformer<UriInfo, org.osehra.integration.http.uri.UriInfo> uriInfoToJaxbUriInfo) {
		this.uriInfoToJaxbUriInfo = uriInfoToJaxbUriInfo;
	}

	@Override
	public Document transform(final UriInfo contextUriInfo)
			throws TransformerException {
		final org.osehra.integration.http.uri.UriInfo uriInformation = this.uriInfoToJaxbUriInfo
				.transform(contextUriInfo);
		try {
			return this.jaxbUtil.marshal(uriInformation);
		} catch (final JAXBException ex) {
			throw new TransformerException(ex);
		}
	}
}
