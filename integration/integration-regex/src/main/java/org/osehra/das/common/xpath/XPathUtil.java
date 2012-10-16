package org.osehra.das.common.xpath;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Asha Amritraj
 */
public class XPathUtil {

	public static XPathExpression compileExpression(final String xQuery) {
		if ((xQuery != null) && (xQuery.length() > 0)) {
			try {
				return XPathFactory.newInstance().newXPath().compile(xQuery);
			} catch (final XPathExpressionException e) {
				throw new RuntimeException(e);
			}
		}
		throw new RuntimeException("Query is null!");
	}

	public static boolean evaluateBoolean(final Document doc,
			final String expression) throws XPathExpressionException {
		final XPathExpression cExpression = XPathUtil
				.compileExpression(expression);
		return (Boolean) cExpression.evaluate(doc, XPathConstants.BOOLEAN);
	}

	public static String getFieldByPath(final Document doc, final String path)
			throws XPathException {

		String fieldValue = "";
		try {
			final NodeList list = XPathAPI.selectNodeList(doc, path);
			if ((list != null) && (list.getLength() > 0)) {
				final Node n = list.item(0);
				if (n != null) {
					final Node text = n.getFirstChild();
					if ((text != null)
							&& (text.getNodeType() == Node.TEXT_NODE)) {
						fieldValue = text.getNodeValue();
					}
				}
			}
		} catch (final TransformerException e2) {
			throw new XPathException(
					"getFieldByPath: transformer exception, path: " + path, e2);
		}
		return fieldValue;
	}

	public static Node getNodeByPath(final Document doc, final String path)
			throws XPathException {

		NodeList list = null;
		try {
			list = XPathAPI.selectNodeList(doc, path);
		} catch (final TransformerException e2) {
			throw new XPathException(
					"getNodeByPath: Could not get the node for XPATH: " + path,
					e2);
		}
		if (list.getLength() > 0) {
			return list.item(0);
		}
		return null;
	}

	/**
	 * Returns a matching Node based on the XPath & Node
	 * 
	 * @param node
	 * @param path
	 * @return Node - the Node that matched
	 * @throws DOMException
	 */
	public static Node getNodeByPath(final Node node, final String path)
			throws XPathException {
		try {
			return XPathAPI.selectSingleNode(node, path);
		} catch (final TransformerException e2) {
			throw new XPathException(
					"getNodeByPath:Could not get the node for XPATH: " + path,
					e2);
		}
	}

	public static NodeList getNodesByPath(final Document doc, final String path)
			throws XPathException {

		NodeList list = null;
		try {
			list = XPathAPI.selectNodeList(doc, path);
		} catch (final TransformerException e2) {
			throw new XPathException(
					"getNodesByPath: Could not get the node for XPATH: " + path,
					e2);
		}
		return list;
	}

	public static NodeList getNodesByPath(final Node node, final String path)
			throws XPathException {

		NodeList list = null;
		try {
			list = XPathAPI.selectNodeList(node, path);
		} catch (final TransformerException e2) {
			throw new XPathException(
					"getNodesByPath:Could not get the node for XPATH: " + path,
					e2);
		}
		return list;
	}
}
