package org.osehra.integration.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * The JAXB Helper.
 *
 * @author Asha Amritraj
 */
public class JaxbUtil implements InitializingBean {

	/**
	 * JAXB Context.
	 *
	 * @uml.property name="context"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	protected JAXBContext context;

	private String factoryName;

    /* BMS - removed due to conflict between Spring constructors and CGLIB
     * See code has been moved to inilization function
     *
	public JaxbUtil(final String factoryName) {
		try {
			this.context = JAXBContext.newInstance(factoryName);
		} catch (final JAXBException ex) {
			throw new RuntimeException(ex);
		}
	}
	*/

	public JaxbUtil() {};

	public JaxbUtil(final String factoryName) {
		setFactoryName(factoryName);
		this.initialize();
	};

	public void initialize() {
		try {
			this.context = JAXBContext.newInstance(this.factoryName);
		} catch (final JAXBException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.initialize();
	}

	@Required
	public void setFactoryName(final String factoryName) {
		this.factoryName = factoryName;
	}

	public Document marshal(final Object obj) throws JAXBException {
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

	public String marshalString(final Object obj) throws JAXBException {
		if (NullChecker.isEmpty(obj)) {
			return null;
		}
		final Marshaller marshaller = this.context.createMarshaller();
		final StringWriter stringWriter = new StringWriter();
		marshaller.marshal(obj, stringWriter);
		return stringWriter.toString();
	}

	public Object unmarshal(final Document doc) throws JAXBException {
		if (NullChecker.isEmpty(doc)) {
			return null;
		}
		final Unmarshaller unmarshaller = this.context.createUnmarshaller();
		return unmarshaller.unmarshal(doc);
	}


}
