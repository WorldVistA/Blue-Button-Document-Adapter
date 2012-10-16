package org.osehra.integration.core.transformer.xml;

import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

/**
 * @author Asha Amritraj
 */
public class XMLToString implements
		org.osehra.integration.core.transformer.Transformer<Document, String> {

	/**
	 * @uml.property name="indent"
	 */
	private int indent = 2;

	/**
	 * @uml.property name="lineWidth"
	 */
	private int lineWidth = 65;
	/**
	 * @uml.property name="prettyPrint"
	 */
	private boolean prettyPrint = false;

	public XMLToString() {

	}

	public XMLToString(final boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	/**
	 * @param indent
	 * @uml.property name="indent"
	 */
	public void setIndent(final int indent) {
		this.indent = indent;
	}

	/**
	 * @param lineWidth
	 * @uml.property name="lineWidth"
	 */
	public void setLineWidth(final int lineWidth) {
		this.lineWidth = lineWidth;
	}

	/**
	 * @param prettyPrint
	 * @uml.property name="prettyPrint"
	 */
	public void setPrettyPrint(final boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	@Override
	public String transform(final Document inputDoc)
			throws TransformerException {
		if (NullChecker.isEmpty(inputDoc)) {
			return null;
		}
		try {
			final Writer writer = new StringWriter();

			if (this.prettyPrint) {
				final OutputFormat format = new OutputFormat(inputDoc);
				format.setLineWidth(this.lineWidth);
				format.setIndenting(true);
				format.setIndent(this.indent);
				final XMLSerializer serializer = new XMLSerializer(writer,
						format);
				serializer.serialize(inputDoc);
			} else {
				final DOMSource domSource = new DOMSource(inputDoc);
				final StreamResult result = new StreamResult(writer);
				final TransformerFactory tf = TransformerFactory.newInstance();
				final Transformer transformer = tf.newTransformer();
				transformer.transform(domSource, result);
				writer.flush();
			}

			return writer.toString();
		} catch (final TransformerConfigurationException e) {
			throw new TransformerException(e);
		} catch (final IOException e) {
			throw new TransformerException(e);
		} catch (final javax.xml.transform.TransformerException e) {
			throw new TransformerException(e);
		} catch (final DOMException e) {
			throw new TransformerException(e);
		}
	}
}
