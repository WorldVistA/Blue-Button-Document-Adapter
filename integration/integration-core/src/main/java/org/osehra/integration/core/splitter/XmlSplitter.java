package org.osehra.integration.core.splitter;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.DOMHelper;
import org.osehra.integration.util.NullChecker;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Split the DOM document by an XPath expression.
 * 
 * @author Julian Jewel
 */
public class XmlSplitter implements Splitter<Document, List<Document>> {

	/**
	 * Add the input message to the output.
	 * 
	 * @uml.property name="addInput"
	 */
	private boolean addInput = false;
	/**
	 * The XPath expression.
	 * 
	 * @uml.property name="expression"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.util.List"
	 */
	private XPathExpression expression;

	/**
	 * Node to XML.
	 * 
	 * @uml.property name="nodeToXML"
	 * @uml.associationEnd
	 */
	private Transformer<Node, Document> nodeToXML;
	/**
	 * String to XML.
	 * 
	 * @uml.property name="stringToXML"
	 * @uml.associationEnd
	 */
	private org.osehra.integration.core.transformer.Transformer<String, Document> stringToXML;

	/**
	 * Set the flag whether to add the input message to the output.
	 * 
	 * @param theAddInput
	 *            true to add the input to the output, false otherwise
	 * @uml.property name="addInput"
	 */
	public void setAddInput(final boolean theAddInput) {
		this.addInput = theAddInput;
	}

	/**
	 * Set the XPath expression to split.
	 * 
	 * @param theExpression
	 *            the XPath expression
	 */
	@Required
	public void setExpression(final String theExpression) {
		try {
			this.expression = XPathFactory.newInstance().newXPath()
					.compile(theExpression);
		} catch (final XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Set the node to XML transformer.
	 * 
	 * @param theNodeToXML
	 *            the node to XML transformer
	 */
	@Required
	public void setNodeToXML(
			final Transformer<Node, Document> theNodeToXML) {
		this.nodeToXML = theNodeToXML;
	}

	/**
	 * The String to XML transformer.
	 * 
	 * @param theStringToXML
	 *            the string to XML transformer
	 */
	@Required
	public void setStringToXML(
			final Transformer<String, Document> theStringToXML) {
		this.stringToXML = theStringToXML;
	}

	/**
	 * Split the document by an XPath expression into an array of documents.
	 * 
	 * @param modelDoc
	 *            the input document
	 * @return the split documents
	 * @throws SplitterException
	 *             an exception occurred during splitting.
	 */
	@Override
	public List<Document> split(final Document modelDoc)
			throws SplitterException {

		Assert.assertNotEmpty(modelDoc, "Document cannot be empty!");

		final List<Document> documents = new ArrayList<Document>();

		try {
			// Cleanup document to assist in splitting
			final Document cleanDoc = DOMHelper.cleaupDocument(modelDoc);
			final Object nodeSet = this.expression.evaluate(cleanDoc,
					XPathConstants.NODESET);
			if (NodeList.class.isInstance(nodeSet)) {
				final NodeList nodeList = (NodeList) nodeSet;
				for (int i = 0; i < nodeList.getLength(); i++) {
					final Node node = nodeList.item(i);
					// Special check for CDATA
					if (CDATASection.class.isInstance(node)) {
						final CDATASection cdata = (CDATASection) nodeList
								.item(i);
						final String data = cdata.getData().trim();
						if (NullChecker.isNotEmpty(data)) {
							final Document doc = this.stringToXML
									.transform(data);
							documents.add(doc);
						} else {
							throw new TransformerException(
									"CDATA segment cannot be empty!");
						}
					} else if (Node.TEXT_NODE == node.getNodeType()) {
						final Document doc = this.stringToXML.transform(node
								.getNodeValue());
						documents.add(doc);
					} else if (Node.ELEMENT_NODE == node.getNodeType()) {
						final Document doc = this.nodeToXML.transform(node);
						documents.add(doc);
					} else if (Node.DOCUMENT_NODE == node.getNodeType()) {
						documents.add((Document) node);
					} else {
						throw new RuntimeException("Unsupported node type!");
					}
				}
			}

			if (this.addInput) {
				documents.add(modelDoc);
			}
			return documents;

		} catch (final XPathExpressionException ex) {
			throw new SplitterException(ex);
		} catch (final TransformerException ex) {
			throw new SplitterException(ex);
		}
	}
}
