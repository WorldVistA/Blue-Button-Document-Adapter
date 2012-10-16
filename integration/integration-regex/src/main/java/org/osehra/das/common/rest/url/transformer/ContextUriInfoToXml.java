package org.osehra.das.common.rest.url.transformer;

import org.osehra.das.common.jaxb.JaxbUtil;
import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * Transform Context URI Information from the REST resource to a XML document.
 * Converts to an intermediate Jaxb Object.
 *
 * @author Asha Amritraj
 *
 */
public class ContextUriInfoToXml implements
		Transformer<UriInfo, Document> {

	private Transformer<UriInfo, org.osehra.das.common.rest.url.UriInfo> uriInfoToJaxbUriInfo;
	private JaxbUtil jaxbUtil;

	@Override
	public Document transform(final UriInfo contextUriInfo)
			throws TransformerException {
		org.osehra.das.common.rest.url.UriInfo uriInformation = this.uriInfoToJaxbUriInfo
				.transform(contextUriInfo);
		try {
			return this.jaxbUtil.marshal(uriInformation);
		} catch (JAXBException ex) {
			throw new TransformerException(ex);
		}
	}

	@Required
	public void setUriInfoToJaxbUriInfo(
			Transformer<UriInfo, org.osehra.das.common.rest.url.UriInfo> uriInfoToJaxbUriInfo) {
		this.uriInfoToJaxbUriInfo = uriInfoToJaxbUriInfo;
	}

	@Required
	public void setJaxbUtil(JaxbUtil jaxbUtil) {
		this.jaxbUtil = jaxbUtil;
	}
}
