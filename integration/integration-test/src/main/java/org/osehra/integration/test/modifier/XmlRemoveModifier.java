package org.osehra.integration.test.modifier;

import org.osehra.integration.util.DOMHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This implementation of modifier removes the select node(s) and its children
 * from a DOM Document.
 *
 * @author Keith Roberts
 *
 */
public class XmlRemoveModifier extends XMLModifier {

	/**
	 * Modifies the source document by removing the node(s) selected by the
	 * specified XPath expression and all their children.
	 */
	@Override
	public Document modifyAux(final Document source) throws ModifyException {
		try {
			final NodeList aNodeList = (NodeList) this.selector.select(source);
			for (int i = 0; i < aNodeList.getLength(); i++) {
				final Node aNode = aNodeList.item(i);
				DOMHelper.removeAllChildNodes(aNode);
				final Node parent = aNode.getParentNode();
				parent.removeChild(aNode);
			}
		} catch (final Exception e) {
			throw new ModifyException(e);
		}
		return source;
	}


}
