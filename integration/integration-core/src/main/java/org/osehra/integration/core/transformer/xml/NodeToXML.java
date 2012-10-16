package org.osehra.integration.core.transformer.xml;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.DOMHelper;
import org.osehra.integration.util.NullChecker;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Convert Node to XML.
 * 
 * @author Julian Jewel
 */
public class NodeToXML implements Transformer<Node, Document> {

	/**
	 * Set this flad to clean the XML document. Default true.
	 * 
	 * @uml.property name="cleanDocument"
	 */
	private boolean cleanDocument = true;

	/**
	 * Set the flag to cleanup the XML document.
	 * 
	 * @param theCleanDocument
	 *            true to clean the document, false otherwise
	 * @uml.property name="cleanDocument"
	 */
	public void setCleanDocument(final boolean theCleanDocument) {
		this.cleanDocument = theCleanDocument;
	}

	/**
	 * Convert the node to document.
	 * 
	 * @param node
	 *            the node
	 * @return the Document
	 * @throws TransformerException
	 *             an exception occurred when converting the node to document
	 */
	@Override
	public Document transform(final Node node)
			throws TransformerException {
		if (NullChecker.isEmpty(node)) {
			return null;
		}
		try {
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			if (this.cleanDocument) {
				doc = DOMHelper.cleaupDocument(doc);
			}
			doc.adoptNode(node);
			doc.appendChild(node);
			return doc;
		} catch (final ParserConfigurationException e) {
			throw new TransformerException(e);
		}
	}
}
