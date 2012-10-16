package org.osehra.integration.test.util.xml;

import org.osehra.integration.core.exception.StackTraceUtil;
import org.osehra.integration.test.modifier.ModifyException;
import org.osehra.integration.test.modifier.XmlCreateModifier;
import org.osehra.integration.test.modifier.XmlRemoveModifier;
import org.osehra.integration.test.modifier.XmlUpdateModifier;
import org.osehra.integration.test.printer.XmlPrinter;
import org.osehra.integration.util.selector.SelectorException;
import org.osehra.integration.util.selector.xml.XPathExpression;
import org.osehra.integration.util.selector.xml.XPathExpressionSelector;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;

import org.junit.Assert;
import org.w3c.dom.Document;

/**
 * Contains convenience methods. Use XmlCreateModifier, XmlUpdateModifier, and
 * XmlRemoveModifier.
 * 
 * @author Keith Roberts
 * 
 */
public class XmlTestUtil {

	// private static final Log log = LogFactory.getLog(XmlTestUtil.class);

	/**
	 * Create a child node of the selected node.
	 * 
	 * @param xPathParentSelector
	 *            - The XPath expression that selects the parent node.
	 * @param rootNode
	 *            - The root node from which the XPath epression is derived.
	 * @param tagName
	 * @param newValue
	 */
	public static void createSelectedNode(final String xPathParentSelector,
			final Document rootNode, final String tagName, final String newValue) {
		final XmlCreateModifier modifier = new XmlCreateModifier();
		final XPathExpression expr = new XPathExpression();
		try {
			expr.compile(xPathParentSelector);
		} catch (final XPathException ex) {
			Assert.fail("XmlTestUtil.CreateSelectedNode failed "
					+ StackTraceUtil.getStackTraceUtilAsString(ex));
		}
		modifier.setXPathExpression(expr);
		modifier.setNewNodeName(tagName);
		modifier.setUpdateValue(newValue);
		try {
			modifier.modify(rootNode);
		} catch (final ModifyException ex) {
			Assert.fail("XmlTestUtil.createSelectedNode failed "
					+ StackTraceUtil.getStackTraceUtilAsString(ex));
		}
	}

	/**
	 * Gets the value of the selected node
	 * 
	 * @param xPathSelector
	 *            - The XPath expression
	 * @param rootNode
	 *            - The root node from which the XPath expression is derived.
	 * @return The selected node value.
	 */
	public static String getSelectedNodeValue(final String xPathSelector,
			final Document rootNode) {
		final XPathExpressionSelector selector = new XPathExpressionSelector();
		final XPathExpression expr = new XPathExpression();
		try {
			expr.compile(xPathSelector);
		} catch (final XPathException ex) {
			Assert.fail("XmlTestUtil.updateSelectedNodeValue failed "
					+ StackTraceUtil.getStackTraceUtilAsString(ex));
		}
		selector.setXPathExpression(expr);
		selector.setReturnType(XPathConstants.STRING);
		Object value = null;
		try {
			value = selector.select(rootNode);
		} catch (final SelectorException ex) {
			Assert.fail("XmlTestUtil.getSelectedNodeValue failed "
					+ StackTraceUtil.getStackTraceUtilAsString(ex));
		}
		return value.toString();
	}

	/**
	 * Is the character in the buffer at position i whitespace.
	 * 
	 * @param buffer
	 *            - The data buffer.
	 * @param i
	 *            - The position.
	 * @return Returns true if the character is whitespace.
	 */
	public static boolean isWhitespace(final StringBuffer buffer, final int i) {
		return (buffer.charAt(i) == ' ') || (buffer.charAt(i) == '\t')
				|| (buffer.charAt(i) == '\n') || (buffer.charAt(i) == '\r');
	}

	/**
	 * Prints the children of the selected node.
	 * 
	 * @param xPathSelector
	 *            - The XPath expression.
	 * @param doc
	 *            - The document to select.
	 */
	public static void printSelectedNode(final String xPathSelector,
			final Document doc) {
		XmlTestUtil.printSelectedNodeValue(xPathSelector, doc);
	}

	/**
	 * Print the selected children of the document.
	 * 
	 * @param xPathSelector
	 *            = The XPath expression.
	 * @param doc
	 *            - The document to select.
	 */
	public static void printSelectedNodeValue(final String xPathSelector,
			final Document doc) {
		final XmlPrinter printer = new XmlPrinter();
		final XPathExpression expr = new XPathExpression();
		try {
			expr.compile(xPathSelector);
		} catch (final XPathException ex) {
			Assert.fail("XmlTestUtil.printSelectedNodeValue failed "
					+ StackTraceUtil.getStackTraceUtilAsString(ex));
		}
		printer.setXPathExpression(expr);
		printer.print(doc);
	}

	/**
	 * Prints the document to StdOut.
	 * 
	 * @param doc
	 *            - The document to print
	 * @throws Exception
	 */
	public static void printXML(final Document doc) throws Exception {
		final XmlPrinter printer = new XmlPrinter();
		printer.print(doc);
	}

	/**
	 * Print the xml string to StdOut.
	 * 
	 * @param xmlString
	 *            - The XML string
	 * @throws Exception
	 */
	public static void printXML(final String xmlString) throws Exception {
		final Document doc = DOMParserHelper.parseDocument(xmlString);
		XmlTestUtil.printXML(doc);
	}

	/**
	 * This convenience method removes the selected node from the root node
	 * document.
	 * 
	 * @param xPathSelector
	 *            - The XPath expression.
	 * @param rootNode
	 *            - The root node from the XPath expression.
	 */
	public static void removeSelectedNode(final String xPathSelector,
			final Document rootNode) {
		final XmlRemoveModifier modifier = new XmlRemoveModifier();
		final XPathExpression expr = new XPathExpression();
		try {
			expr.compile(xPathSelector);
		} catch (final XPathException ex) {
			Assert.fail("XmlTestUtil.updateSelectedNodeValue failed "
					+ StackTraceUtil.getStackTraceUtilAsString(ex));
		}
		modifier.setXPathExpression(expr);
		try {
			modifier.modify(rootNode);
		} catch (final ModifyException ex) {
			Assert.fail("XmlTestUtil.updateSelectedNodeValue failed "
					+ StackTraceUtil.getStackTraceUtilAsString(ex));
		}
	}

	/**
	 * Removes the whitespace in the buffer. This function assumes that the
	 * buffer contains a serialized XML document.
	 * 
	 * @param buffer
	 *            - The data buffer
	 * @param i
	 *            - The position of the starting character.
	 */
	public static void removeWhitespace(final StringBuffer buffer, final int i) {
		boolean foundWhitespace = false;
		for (int k = i; k < buffer.length(); k++) {
			if (!XmlTestUtil.isWhitespace(buffer, k)) {
				if (foundWhitespace) {
					// System.out.println("delete="+buffer.substring(i-10,i)+"'"+buffer.substring(i,k)+"'"+buffer.substring(k,k+10));
					buffer.delete(i, k);
				}
				return;
			} else {
				foundWhitespace = true;
			}
		}
	}

	/**
	 * Removes all the whitespace from the string.
	 * 
	 * @param data
	 *            - The input string.
	 * @return Returns the string without whitespace.
	 */
	public static String removeWhiteSpace(final String data) {
		final StringBuffer buffer = new StringBuffer(data);
		int i = buffer.indexOf("?>") + 2;
		if (!XmlTestUtil.isWhitespace(buffer, i)) {
			buffer.insert(i, "\n");
			i++;
		}
		for (; i + 1 < buffer.length(); i++) {
			if ((buffer.charAt(i) == '>')
					&& XmlTestUtil.isWhitespace(buffer, i + 1)) {
				XmlTestUtil.removeWhitespace(buffer, i + 1);
			}
			final int idx = buffer.indexOf("&gt;", i);
			if ((idx >= 0) && (idx == i)
					&& XmlTestUtil.isWhitespace(buffer, i + 4)) {
				XmlTestUtil.removeWhitespace(buffer, i + 4);
			}
		}
		return buffer.toString();
	}

	/**
	 * This convenience method update the selected node with the new value
	 * 
	 * @param xPathSelector
	 *            = The XPath expression.
	 * @param rootNode
	 *            - The root node of the XPath expression.
	 * @param newValue
	 *            = The new value to update the selected node.
	 */
	public static void updateSelectedNodeValue(final String xPathSelector,
			final Document rootNode, final String newValue) {
		final XmlUpdateModifier modifier = new XmlUpdateModifier();
		final XPathExpression expr = new XPathExpression();
		try {
			expr.compile(xPathSelector);
		} catch (final XPathException ex) {
			Assert.fail("XmlTestUtil.updateSelectedNodeValue failed "
					+ StackTraceUtil.getStackTraceUtilAsString(ex));
		}
		modifier.setXPathExpression(expr);
		modifier.setUpdateValue(newValue);
		try {
			modifier.modify(rootNode);
		} catch (final ModifyException ex) {
			Assert.fail("XmlTestUtil.updateSelectedNodeValue failed "
					+ StackTraceUtil.getStackTraceUtilAsString(ex));
		}
	}

}
