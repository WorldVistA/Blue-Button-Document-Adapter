package org.osehra.integration.test.modifier;

import org.osehra.integration.util.DOMHelper;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A modifier implementation that will create a node in a DOM Document as a
 * child to the selected node. If a list is selected, the new node is added to
 * each node in the list.
 *
 * @author Keith Roberts
 *
 */
public class XmlCreateModifier extends XmlUpdateModifier {

	private String newNodeName;

	/**
	 * Modifies the source document by adding a new node with the given name and
	 * update value as a child to the node selected from the specified XPath
	 * expression.
	 */
	@Override
	public Document modifyAux(final Document source) throws ModifyException {
		try {
			final NodeList aNodeList = (NodeList) this.selector.select(source);
			for (int i = 0; i < aNodeList.getLength(); i++) {
				final Node aNode = aNodeList.item(i);
				if (this.getUpdateValue() != null) {
					DOMHelper.createNodeAndValueInSequence(aNode,
							this.newNodeName, this.getUpdateValue());
				} else {
					DOMHelper.createNode(aNode, this.newNodeName);
				}
			}
		} catch (final Exception e) {
			throw new ModifyException(e);
		}
		return source;
	}

	/**
	 * Sets the name of the new node to be added.
	 *
	 * @param name
	 *            - The name of the new node.
	 */
	@Required
	public void setNewNodeName(final String name) {
		this.newNodeName = name;
	}

}
