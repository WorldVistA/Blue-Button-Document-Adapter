package org.osehra.integration.test.util.xml;

import org.osehra.integration.util.DOMHelper;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A class to parse the String into a DOM Document Object.
 * 
 * @author Julian Jewel
 */
public class DOMParserHelper {

	private static final Log log = LogFactory.getLog(DOMParserHelper.class);

	/**
	 * Parses the specified input stream into a Document object.
	 * 
	 * @param stream
	 *            - The input stream.
	 * @return The XML DOM document.
	 */
	public static Document parseDocument(final InputStream stream)
			throws DOMException {
		try {
			final Document doc = DOMHelper.getDocumentBuilder().parse(stream);
			// doc.normalize();
			// removeEmptyTextNodes(doc.getDocumentElement());
			return doc;
		} catch (final java.io.IOException ex) {
			throw new DOMException("DOMParserHelper failed to parse", ex);
		} catch (final SAXException ex) {
			throw new DOMException("DOMParserHelper failed to parse", ex);
		}
	}

	/**
	 * Parses the specified Source into a Document object.
	 * 
	 * @param source
	 *            - The XML document source.
	 * @return The XML DOM document.
	 */
	public static Document parseDocument(final Source source)
			throws DOMException {
		try {
			if (source instanceof StreamSource) {
				// long begMemBytes = MemUtil.getUsedMemory();
				Document doc = null;
				final StreamSource streamSource = (StreamSource) source;
				final Reader reader = streamSource.getReader();
				if (reader != null) {
					doc = DOMHelper.getDocumentBuilder().parse(
							new InputSource(reader));
					// MemUtil.logMemory("Parse XML","reader",begMemBytes);
					// doc.normalize();
					// removeEmptyTextNodes(doc.getDocumentElement());
					return doc;
				}
				final InputStream inputStream = streamSource.getInputStream();
				if (inputStream != null) {
					doc = DOMHelper.getDocumentBuilder().parse(inputStream);
					// MemUtil.logMemory("Parse XML","is",begMemBytes);
					// doc.normalize();
					// removeEmptyTextNodes(doc.getDocumentElement());
					return doc;
				}
				final String url = ((StreamSource) source).getSystemId();
				if (url != null) {
					doc = DOMHelper.getDocumentBuilder().parse(url);
					// MemUtil.logMemory("Parse XML","source",begMemBytes);
					// doc.normalize();
					// removeEmptyTextNodes(doc.getDocumentElement());
					return doc;
				}
				DOMParserHelper.log
						.error("Unknown stream source parsing handle");
				return null;
			} else if (source instanceof DOMSource) {
				final Node doc = ((DOMSource) source).getNode();
				if (doc instanceof Document) {
					// doc.normalize();
					// removeEmptyTextNodes(doc);
					return (Document) doc;
				}
				DOMParserHelper.log.info("DOMSource node is of type: "
						+ doc.getClass().getName());
				return null;
			}
			DOMParserHelper.log.info("Unknown source "
					+ source.getClass().getName());
			return null;
		} catch (final SAXException ex) {
			throw new DOMException("DOMParserHelper.parseDocument failed", ex);
		} catch (final java.io.IOException ex) {
			throw new DOMException("DOMParserHelper failed to parse", ex);
		}
	}

	/**
	 * Parses the specified serial xml string into a Document object.
	 * 
	 * @param str
	 *            - the XML string.
	 * @return An XML DOM document.
	 */
	public static Document parseDocument(final String str) throws DOMException {
		return DOMParserHelper.parseDocument(new StreamSource(new StringReader(
				str)));
	}

}
