package org.osehra.das.common.transformer.xml;

import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.validation.NullChecker;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Asha Amritraj
 */
public class StringToXML
		implements
		org.osehra.das.common.transformer.Transformer<String, Document> {

	public StringToXML() {
	}

	@Override
	public final Document transform(final String srcString)
			throws org.osehra.das.common.transformer.TransformerException {
		if (NullChecker.isEmpty(srcString)) {
			return null;
		}
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document doc = builder.parse(new InputSource(
					new StringReader(srcString)));
			if (NullChecker.isEmpty(doc)) {
				throw new RuntimeException("Document cannot be null!");
			}
			return doc;
		} catch (final ParserConfigurationException ex) {
			throw new TransformerException(ex);
		} catch (final FactoryConfigurationError ex) {
			throw new TransformerException(ex);
		} catch (final SAXException ex) {
			throw new TransformerException(ex);
		} catch (final IOException ex) {
			throw new TransformerException(ex);
		}
	}
}
