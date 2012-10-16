package org.osehra.integration.test.modifier;

import org.osehra.integration.util.selector.xml.XPathExpression;
import org.osehra.integration.util.selector.xml.XPathExpressionSelector;

import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;

import org.w3c.dom.Document;

public abstract class XMLModifier implements Modifier<Document, Document> {

	protected XPathExpressionSelector selector = new XPathExpressionSelector();
	private Map<String,String> namespaceMap = null;

	@Override
	public Document modify(final Document source) throws ModifyException {
		Document modifiedDocument;

		// If namespaceMap was assigned, apply it to the expression
		if (namespaceMap != null) {
			XPathExpression expr = this.selector.getExpression();
			String exprString = expr.toString();
			NamespaceContext nc = new NamespaceContextImpl(namespaceMap);
			try {
				expr.compile(exprString, nc);
			} catch (final XPathException e) {
				throw new ModifyException(e);
			}
			this.setXPathExpression(expr);
		}

		modifiedDocument = this.modifyAux(source);
		return modifiedDocument;
	}

	// Stub.  Overridden in subclasses.
	public abstract Document modifyAux(final Document source) throws ModifyException;

	/**
	 * Sets the namespaceMap for the XPath expression.
	 *
	 * @param map
	 *            - A map where the names are namespace references
	 *             and the values are the associated URIs.
	 */
	public void setNamespaceMap(final Map<String, String> map) {
		this.namespaceMap = map;
	}

	/**
	 * Sets the xPath express that selects the node to be modified
	 *
	 * @param expression
	 *            - The XPath expression.
	 */
	public void setXPathExpression(final XPathExpression expression) {
		this.selector.setReturnType(XPathConstants.NODESET);
		this.selector.setXPathExpression(expression);
	}


}
