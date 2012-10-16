package org.osehra.das.common.jaxb;

import org.osehra.das.common.validation.NullChecker;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

/**
 * The JAXB Helper.
 * 
 * @author Asha Amritraj
 */
public class JaxbUtil {

	/**
	 * JAXB Context.
	 * @uml.property  name="context"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JAXBContext context;

	public JaxbUtil(final String factoryName) {
		try {
			this.context = JAXBContext.newInstance(factoryName);
		} catch (final JAXBException ex) {
			throw new RuntimeException(ex);
		}
	}

	public final Document marshal(final Object obj) throws JAXBException {
		try {
			if (NullChecker.isEmpty(obj)) {
				return null;
			}
			final Marshaller marshaller = this.context.createMarshaller();
			final Document marshalledDoc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			marshaller.marshal(obj, marshalledDoc);
			return marshalledDoc;
		} catch (final ParserConfigurationException ex) {
			throw new JAXBException(ex);
		}
	}

	public final String marshalString(final Object obj) throws JAXBException {
		if (NullChecker.isEmpty(obj)) {
			return null;
		}
		final Marshaller marshaller = this.context.createMarshaller();
		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(obj, stringWriter);
		return stringWriter.toString();
	}

	public final Object unmarshal(final Document doc) throws JAXBException {
		if (NullChecker.isEmpty(doc)) {
			return null;
		}
		final Unmarshaller unmarshaller = this.context.createUnmarshaller();
		return unmarshaller.unmarshal(doc);
	}

}
