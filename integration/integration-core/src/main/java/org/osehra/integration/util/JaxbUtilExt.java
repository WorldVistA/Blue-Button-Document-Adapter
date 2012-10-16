package org.osehra.integration.util;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * A typical root element issue with Marshalling. Override JAXBUtil to add root
 * element for specific classes.
 * 
 * @author Asha Amritraj
 * 
 */
public class JaxbUtilExt extends JaxbUtil {

	Class<?> clazz;
	String localPart;
	String namespace;

	public JaxbUtilExt(final String factoryName) {
		super(factoryName);
	}

	@Override
	public Document marshal(final Object obj) throws JAXBException {
		try {
			if (NullChecker.isEmpty(obj)) {
				return null;
			}
			final Marshaller marshaller = this.context.createMarshaller();
			final Document marshalledDoc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			marshaller.marshal(new JAXBElement(new QName(this.namespace,
					this.localPart), this.clazz, obj), marshalledDoc);
			return marshalledDoc;
		} catch (final ParserConfigurationException ex) {
			throw new JAXBException(ex);
		}
	}

	@Required
	public void setClazz(final Class<?> clazz) {
		this.clazz = clazz;
	}

	@Required
	public void setLocalPart(final String localPart) {
		this.localPart = localPart;
	}

	@Required
	public void setNamespace(final String namespace) {
		this.namespace = namespace;
	}

}
