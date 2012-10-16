package org.osehra.integration.test.modifier;

import org.osehra.integration.util.Assert;
import org.osehra.integration.util.DOMHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A modifier implementation that updates the selected node with an update
 * value. If a list is selected, each node in the list is updated.
 *
 * @author Keith Roberts
 *
 */
public class XmlUpdateModifier extends XMLModifier {

	private String updateValue;
//	protected XPathExpressionSelector selector = new XPathExpressionSelector();
//	private Map<String,String> namespaceMap = null;

	/**
	 * Gets the value to update.
	 *
	 * @return The value to update.
	 */
	public String getUpdateValue() {
		return this.updateValue;
	}

	/**
	 * Modifies the document by selecting the node(s) with the specified XPath
	 * expression and updating the value with the specified update value. An
	 * exception is thrown if the node does not exist.
	 */
	@Override
	public Document modifyAux(final Document source) throws ModifyException {
		Assert.assertNotEmpty(source, "The source document cannot be null");
		try {
			final NodeList aNodeList = (NodeList) this.selector.select(source);
			for (int i = 0; i < aNodeList.getLength(); i++) {
				final Node aNode = aNodeList.item(i);
				DOMHelper.updateNodeValue(aNode, this.updateValue);
			}
		} catch (final Exception e) {
			throw new ModifyException(e);
		}
		return source;
	}

//	@Override
//	public Document modify(final Document source) throws ModifyException {
//		Document modifiedDocument;
//
//		// If namespaceMap was assigned, apply it to the expression
//		if (namespaceMap != null) {
//			XPathExpression expr = this.selector.getExpression();
//			String exprString = expr.toString();
//			NamespaceContext nc = new NamespaceContextImpl(namespaceMap);
//			try {
//				expr.compile(exprString, nc);
//			} catch (final XPathException e) {
//				throw new ModifyException(e);
//			}
//			this.setXPathExpression(expr);
//		}
//
//		modifiedDocument = this.modifyAux(source);
//		return modifiedDocument;
//	}

//	/**
//	 * Sets the namespaceMap for the XPath expression.
//	 *
//	 * @param map
//	 *            - A map where the names are namespace references
//	 *             and the values are the associated URIs.
//	 */
//	public void setNamespaceMap(final Map<String, String> map) {
//		this.namespaceMap = map;
//	}

	/**
	 * Sets the value to update.
	 *
	 * @param value
	 *            - The value to update.
	 */
	public void setUpdateValue(final String value) {
		this.updateValue = new String(value);
	}

//	/**
//	 * Sets the xPath express that selects the node to be modified
//	 *
//	 * @param expression
//	 *            - The XPath expression.
//	 */
//	public void setXPathExpression(final XPathExpression expression) {
//		this.selector.setReturnType(XPathConstants.NODESET);
//		this.selector.setXPathExpression(expression);
//	}

}
