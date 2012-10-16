package org.osehra.integration.test.runner;

import org.osehra.integration.core.transformer.xsl.DefaultXSLTransformer;
import org.osehra.integration.util.resolver.Resolver;
import org.osehra.integration.util.xsl.ResolvableXSLTransformer;
import org.osehra.integration.util.xsl.XSLTransformer;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XSLTransformerTestRunner extends AbstractTestRunner {

	private XSLTransformer transformer;
	private boolean isXmlOutput = true;

	@Override
	boolean isXmlInput() {
		return true;
	}

	@Override
	protected Object transduce() {
		Object result;
		final DOMSource source = new DOMSource((Document) input);
		final StringWriter writer = new StringWriter();
		try {
			this.transformer.transform(source, new StreamResult(writer),
					this.parameters);
			if (this.isXmlOutput) {
				result = this.transformStringToXML(writer.toString());
			} else {
				result = writer.toString();
			}
		} catch (final TransformerException ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}

	public void setDoXmlOutput(final boolean flag) {
		this.isXmlOutput = flag;
	}

	/**
	 * Sets the filename of the XSL file to test that is found based on the
	 * policy of the Spring application context..
	 *
	 * @param xslFilename
	 *            - The XSL file to test.
	 */
	public void setXslFilename(final String xslFilename) {
		if (this.transformer instanceof DefaultXSLTransformer) {
			((DefaultXSLTransformer) this.transformer)
					.setResource(this.applicationContext
							.getResource(xslFilename));
		} else if (this.transformer instanceof ResolvableXSLTransformer) {
			((ResolvableXSLTransformer) this.transformer)
					.setResolver(new Resolver<Document, Resource>() {
						@Override
						public Resource resolve(final Document doc) {
							return XSLTransformerTestRunner.this.applicationContext
									.getResource(xslFilename);
						}
					});
		} else {
			throw new RuntimeException("Unknown XSLTransfomer type "
					+ this.transformer.getClass().getName());
		}
	}

	/**
	 * Sets the transformer that will perform the XSL transformation.
	 *
	 * @param transformer
	 *            - The transformer used to perform the transformation.
	 */
	@Required
	public void setTransformer(final XSLTransformer transformer) {
		this.transformer = transformer;
	}

	public final Document transformStringToXML(final String srcString) {
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document doc = builder.parse(new InputSource(
					new StringReader(srcString)));
			return doc;
		} catch (final ParserConfigurationException ex) {
			throw new RuntimeException(ex);
		} catch (final FactoryConfigurationError ex) {
			throw new RuntimeException(ex);
		} catch (final SAXException ex) {
			throw new RuntimeException(ex);
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
