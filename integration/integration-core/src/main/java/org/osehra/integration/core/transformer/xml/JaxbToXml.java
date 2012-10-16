package org.osehra.integration.core.transformer.xml;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.JaxbUtil;

import javax.xml.bind.JAXBException;

import org.w3c.dom.Document;

public class JaxbToXml implements Transformer<Object, Document> {

	JaxbUtil jaxbUtil;

	public void setJaxbUtil(final JaxbUtil jaxbUtil) {
		this.jaxbUtil = jaxbUtil;
	}

	@Override
	public Document transform(final Object src) throws TransformerException {
		try {
			return this.jaxbUtil.marshal(src);
		} catch (final JAXBException ex) {
			throw new TransformerException(ex);
		}
	}
}
