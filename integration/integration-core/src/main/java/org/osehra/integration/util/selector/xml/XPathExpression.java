package org.osehra.integration.util.selector.xml;

import javax.xml.namespace.QName;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;

/**
 * This class wraps javax.xml.xpath.XPathExpression so that
 * it maintains a printable expression string, since there is no
 * no way to obtain it from the javax.xml.xpath.XPathExpression
 * interface. This is useful for exception messages and
 * property editor implementation.
 *
 * @author Keith Roberts
 *
 */
public class XPathExpression {

	/**
	 * The XPathExpression interface.
	 */
	private javax.xml.xpath.XPathExpression expression;
	/**
	 * The text representation of the expression.
	 */
	private String expressionString;

	/**
	 * Compiles the expression string into an XPathExpression
	 * instance.
	 * @param expr - The expression string.
	 */
	public void compile(String expr) throws javax.xml.xpath.XPathException{
		this.expressionString = expr;
		this.expression = XPathFactory.newInstance().newXPath().compile(
			expr);
	}

	/**
	 * Compiles the expression string into an XPathExpression utilizing a NameSpaceContext
	 * instance.
	 * @param expr - The expression string.
	 * @param nsContext - The NameSpaceContext object to be used
	 */
	public void compile(String expr, NamespaceContext nsContext) throws javax.xml.xpath.XPathException{
		this.expressionString = expr;
		XPath path = (XPath)XPathFactory.newInstance().newXPath();
		path.setNamespaceContext(nsContext);
		this.expression = path.compile(expr);
	}

	/**
	 * Evaluates the XPathExpression on the document to select
	 * the node object specified in the expression.
	 * @param sourceData - The XML document to evaluate.
	 * @param returnType - The name of the return type. May be
	 * either XPathConstants.STRING, XPathConstants.NODE, or
	 * XPathConstants.NODE_SET.
	 * @return - Returns an object of either type String, Node,
	 * or NodeList; depending on the returnType value.
	 */
	public Object evaluate(final Document sourceData, final QName returnType)  throws javax.xml.xpath.XPathException{
		return expression.evaluate(sourceData,returnType);
	}

	/**
	 * Returns the string representation of the expression.
	 */
	public String toString() {
		return this.expressionString;
	}
}
