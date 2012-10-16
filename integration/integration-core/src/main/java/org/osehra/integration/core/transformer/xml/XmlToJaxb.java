package org.osehra.integration.core.transformer.xml;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.JaxbUtil;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.w3c.dom.Document;

public class XmlToJaxb implements Transformer<Document, Object> {

	JaxbUtil jaxbUtil;

	public void setJaxbUtil(final JaxbUtil jaxbUtil) {
		this.jaxbUtil = jaxbUtil;
	}

	@Override
	public Object transform(final Document src) throws TransformerException {
		try {
			final Object obj = this.jaxbUtil.unmarshal(src);
			if (JAXBElement.class.isInstance(obj)) {
				return ((JAXBElement<?>) obj).getValue();
			}
			return obj;
		} catch (final JAXBException ex) {
			throw new TransformerException(ex);
		}
	}
}
