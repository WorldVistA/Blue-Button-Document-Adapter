package org.osehra.integration.test.util.xml;

import java.io.IOException;
import java.io.StringWriter;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 * A class to produce a Serialized String from DOM Document.
 * 
 * @author Julian Jewel
 */
public class DOMSerializerHelper {

	/**
	 * Returns DOM implementation used for serialization
	 * 
	 * @return
	 * @throws DOMException
	 */
	protected static DOMImplementationLS getDOMImpl() throws DOMException {

		DOMImplementationRegistry registry;

		try {
			registry = DOMImplementationRegistry.newInstance();
		} catch (final Exception e) {
			throw new DOMException(e);
		}
		final DOMImplementationLS domImpl = (DOMImplementationLS) registry
				.getDOMImplementation("LS");

		return domImpl;
	}

	/**
	 * Returns an XML string from the specified DOM document.
	 * 
	 * @param xmlDoc
	 *            - The DOM document.
	 * @return String - The XMl string.
	 * @throws IOException
	 */
	public static String serializeDocument(final Document xmlDoc)
			throws DOMException {
		return DOMSerializerHelper.serializeDocument(
				xmlDoc.getDocumentElement(), false, false, false);
	}

	/**
	 * Returns an XML string from the specified DOM document.
	 * 
	 * @param xmlDoc
	 *            - The DOM document.
	 * @param format
	 *            - true or false
	 * @return String - The serialized string of the XML Document.
	 * @throws IOException
	 */
	public static String serializeDocument(final Document xmlDoc,
			final boolean format) throws DOMException {
		return DOMSerializerHelper.serializeDocument(
				xmlDoc.getDocumentElement(), format, false, false);
	}

	/**
	 * Returns a Serialized Document
	 * 
	 * @param xmlDoc
	 *            - the DOM document
	 * @param format
	 *            - true or false
	 * @param omitDecl
	 *            - true or false
	 * @return String - the Serialized XML Document
	 * @throws DOMException
	 */
	public static String serializeDocument(final Document xmlDoc,
			final boolean format, final boolean omitDecl,
			final boolean stripComments) throws DOMException {

		return DOMSerializerHelper.serializeDocument(
				xmlDoc.getDocumentElement(), format, omitDecl, stripComments);
	}

	/**
	 * Returns an XML string from the specified DOM element.
	 * 
	 * @param xmlElem
	 *            - The DOM element.
	 * @return String - The XML string.
	 * @throws IOException
	 */
	public static String serializeDocument(final Element xmlElem)
			throws DOMException {
		return DOMSerializerHelper.serializeDocument(xmlElem, false, false,
				false);
	}

	/**
	 * Returns an XML string from the specified DOM element.
	 * 
	 * @param xmlElem
	 *            - The DOM element.
	 * @param format
	 *            - true or false
	 * @param omitDecl
	 *            - true or false
	 * @param stripComments
	 *            - true or false
	 * @return String - the XML string.
	 * @throws DOMException
	 */
	public static String serializeDocument(final Element xmlElem,
			final boolean format, final boolean omitDecl,
			final boolean stripComments) throws DOMException {

		final DOMImplementationLS domImpl = DOMSerializerHelper.getDOMImpl();
		final LSSerializer writer = domImpl.createLSSerializer();
		final DOMConfiguration config = writer.getDomConfig();
		if (format) {
			config.setParameter("format-pretty-print", true);
		}
		if (omitDecl) {
			config.setParameter("xml-declaration", false);
		}
		if (stripComments) {
			config.setParameter("comments", false);
		}
		final LSOutput output = domImpl.createLSOutput();
		final StringWriter sw = new StringWriter();
		output.setCharacterStream(sw);
		writer.write(xmlElem, output);

		return sw.toString();

	}

}
