/*
 * Created on Sep 13, 2005 This is copyrighted software of the United States
 * Federal Government. Any use must be authorized by the Department of Veterans
 * Affairs OI Field Office - Salt Lake City Health Data Systems Any
 * un-authorized use is strictly prohibited and subject to legal action.
 */
package org.osehra.das.common.xml;

import org.osehra.das.common.validation.NullChecker;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author Keith Roberts A class containing static XML convenience methods.
 */
public class DOMHelper {

	private static final Log log = LogFactory.getLog(DOMHelper.class);

	/**
	 * Insert the node as a child node to the specified parent.
	 * 
	 * @param parent
	 *            - The parent node.
	 * @param child
	 *            - The child node that is inserted.
	 * @return
	 * @throws DOMException
	 */
	// Insert Child node based on the order
	// @return the appeneded child node
	public static Node appendChildInSequence(final Node parent, final Node child)
			throws DOMException {
		if ((parent != null) && (child != null)) {
			final String nodeName = child.getNodeName();
			final String[] splitNameIndex = nodeName.split("\\.");
			// KLUDGE - Handle only Text.Number format
			if ((splitNameIndex != null) && (splitNameIndex.length == 2)) {
				final String name = splitNameIndex[0];
				final String index = splitNameIndex[1];
				if (index.matches("[0-9]*")) { // Is it really a number
					try {
						final int seq = Integer.parseInt(index);
						if (seq == 1) { // If CE.1 - insert right below the
							// parent as first child
							parent.insertBefore(child, parent.getFirstChild());
							return child;
						}
						if (seq > 1) { // Assume that all tags start with 1
							for (int i = seq - 1; i >= 1; i--) { // Go
								// reverse
								final String seqStr = name + "."
										+ Integer.toString(i);
								final Node prevSibling = DOMHelper.getChild(
										parent, seqStr);
								if (prevSibling != null) {
									DOMHelper.insertAfter(parent, child,
											prevSibling);
									return child;
								}
							}
						}
					} catch (final NumberFormatException ex) {
						throw new RuntimeException(
								"appendChild:Wrong number format at Name: "
										+ name, ex);
					}
				}
			}
			parent.appendChild(child);
			return child;
		}
		return child;
	}

	/**
	 * Removes all the junk nodes from the document.
	 * 
	 * @param doc
	 *            - The input document.
	 * @return The modified document.
	 */
	public static Document cleaupDocument(final Document doc) {
		if (NullChecker.isEmpty(doc)) {
			return null;
		}

		final List<Node> toRemoveList = new ArrayList<Node>();
		DOMHelper.parseJunkNodes(doc, toRemoveList, false);
		for (final Node node : toRemoveList) {
			node.getParentNode().removeChild(node);
		}
		return doc;
	}

	/**
	 * Removes all the junk nodes from the document.
	 * 
	 * @param doc
	 *            - The input document.
	 * @param removeComments
	 *            - Removes all comments from the document.
	 * @return The modified document.
	 */
	public static Document cleaupDocument(final Document doc,
			final boolean removeComments) {
		if (NullChecker.isEmpty(doc)) {
			return null;
		}

		final List<Node> toRemoveList = new ArrayList<Node>();
		DOMHelper.parseJunkNodes(doc, toRemoveList, removeComments);
		for (final Node node : toRemoveList) {
			node.getParentNode().removeChild(node);
		}
		return doc;
	}

	/**
	 * Clone all the nodes of the source node and append them to the destination
	 * nodes
	 */
	public static void cloneChildren(final Node source, final Node destination) {
		DOMHelper.cloneChildren(source, destination, false);
	}

	/**
	 * Clone all the nodes of the source node and append them to the destination
	 */
	public static void cloneChildren(final Node source, final Node destination,
			final boolean canRemove) {
		if ((source != null) && (destination != null)) {
			final NodeList nodeList = source.getChildNodes();
			if (nodeList != null) {
				// Remove all children before adding to the node
				if (canRemove) {
					DOMHelper.removeAllChildNodes(destination);
				}
				for (int i = 0; i < nodeList.getLength(); i++) {
					final Node childNode = nodeList.item(i);
					if (childNode != null) {
						destination.appendChild(childNode.cloneNode(true));
					}
				}
			}
		}
	}

	/*
	 * public static int getDepth( Node node, int depth ) { if (node != null) {
	 * NodeList list = node.getChildNodes(); if (list == null ||
	 * list.getLength() == 0) { return depth; } for (int i = 0; i <
	 * list.getLength(); i++) { Node curNode = list.item(i); depth++; return
	 * getDepth(curNode,depth); } } return -1; }
	 */

	/**
	 * Create a child node, even if it exists
	 */
	public static Node createChildInSequence(final Node parent,
			final String name) throws DOMException {
		if (parent == null) {
			throw new RuntimeException("createChildInSequence: null parent");
		}
		final Document doc = parent.getOwnerDocument();
		if (doc == null) {
			throw new RuntimeException(
					"createChildInSequence: null owner document");
		}
		final Node createNode = doc.createElement(name);
		// Append it to the parent
		DOMHelper.appendChildInSequence(parent, createNode);
		return createNode;
	}

	/**
	 * Create child sequence with a generated prefix
	 */
	public static Node createChildSequence(final Node parent,
			final String[] values, final String prefix) throws DOMException {
		if ((parent != null) && (values != null) && (values.length > 0)) {
			final String[] prefixes = new String[values.length];
			for (int i = 0; i < prefixes.length; i++) {
				prefixes[i] = prefix + "." + Integer.toString(i + 1);
			}
			return DOMHelper.createChildSequence(parent, values, prefixes);
		}
		return null;
	}

	/**
	 * Create child nodes based on a prefix If you wish to create CE.1, CE.2
	 * etc.. the prefix would be CE And the values are substituted based on the
	 * prefix
	 */
	public static Node createChildSequence(final Node parent,
			final String[] values, final String[] nodeNames)
			throws DOMException {
		if ((parent != null) && (values != null)
				&& (values.length == nodeNames.length)) {
			for (int i = 0; i < values.length; i++) { // Put all values into
				// children
				DOMHelper.createNodeAndValueInSequence(parent, nodeNames[i],
						values[i]);
			}
		}
		return parent;
	}

	/**
	 * Create child sequence with Coded element
	 */
	public static Node createChildSequenceCE(final Node parent,
			final String[] values) throws DOMException {
		return DOMHelper.createChildSequence(parent, values, "CE");
	}

	/**
	 * Create child sequence with Hierarchic designator
	 */
	public static Node createChildSequenceHD(final Node parent,
			final String[] values) throws DOMException {
		return DOMHelper.createChildSequence(parent, values, "HD");
	}

	/**
	 * Creates a child node with the specified name to the parent.
	 * 
	 * @param parent
	 * @param tagName
	 * @return
	 */
	public static Node createNode(final Node parent, final String tagName) {
		return DOMHelper.createNode(parent, tagName, false);
	}

	/*
	 * public static boolean clearNode(Node node ) { Node firstChild = null;
	 * while ((firstChild = node.getFirstChild()) != null) { Node nextSibling =
	 * firstChild.getNextSibling(); do { if (nextSibling != null) {
	 * clearNode(nextSibling); } Node saveSibling = nextSibling;
	 * nextSibling.getParentNode().removeChild(nextSibling); } while
	 * (((nextSibling = nextSibling.getNextSibling()) != null));
	 * firstChild.getParentNode().removeChild(firstChild); } return true; }
	 */

	/**
	 * Creates a child node with the specified name to the parent.
	 * 
	 * @param parent
	 *            - The parent node.
	 * @param tagName
	 *            - The name of the new node.
	 * @param createValueNode
	 *            - If true create a text value for the node.
	 * @return
	 */
	public static Node createNode(final Node parent, final String tagName,
			final boolean createValueNode) {
		final Document doc = parent.getOwnerDocument();
		final Element updateEle = doc.createElement(tagName);
		parent.appendChild(updateEle);
		// Create Dummy text
		Text text = doc.createTextNode("\n");
		parent.appendChild(text);
		if (createValueNode) {
			text = doc.createTextNode("D");
			updateEle.appendChild(text);
		}
		return updateEle;

	}

	/**
	 * Create node and value - if the node is found - then the text node is
	 * replaced
	 */
	public static Node createNodeAndValueInSequence(final Node parent,
			final String nodeName, final String nodeTextValue)
			throws DOMException {
		final Node nCE = DOMHelper.getOrCreateChildNode(parent, nodeName);
		DOMHelper.getOrCreateTextNode(nCE, nodeTextValue);
		return nCE;
	}

	/**
	 * Finds the first node of the specified type.
	 * 
	 * @param node
	 *            - The root node.
	 * @param type
	 *            - The type for which to search.
	 * @return - The first node of the specified type.
	 */
	public static Node findNodeByType(Node node, final int type) {
		if (node.getNodeType() == type) {
			String value = node.getNodeValue();
			if (value != null) {
				value = value.trim();
				if (value.length() > 0) {
					node.setNodeValue(value);
					return node;
				}
			}
		}
		final NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			final Node curNode = children.item(i);
			node = DOMHelper.findNodeByType(curNode, type);
			if (node != null) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Get child - would return null if the child is not present. Does not
	 * insert a node if the child is not present
	 */
	public static Node getChild(final Node parent, final String name) {
		if ((parent != null) && (name != null) && (name.length() > 0)) {
			if (parent.hasChildNodes()) { // No child - then create one
				final NodeList nodes = parent.getChildNodes();
				if (nodes != null) {
					for (int i = 0; i < nodes.getLength(); i++) {
						final Node node = nodes.item(i); // We have children
						if (node != null) {
							if (node.getNodeName().equals(name)) {
								return node;
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets a document build instance.
	 * 
	 * @return
	 */
	public static DocumentBuilder getDocumentBuilder() {
		DocumentBuilder db = null;
		try {
			final DocumentBuilderFactory dbf = DocumentBuilderFactory
					.newInstance();
			dbf.setNamespaceAware(true);
			db = dbf.newDocumentBuilder();
		} catch (final ParserConfigurationException ex) {
			DOMHelper.log.error("ERROR: Document builder creation failed: "
					+ ex);
		}
		return db;
	}

	/**
	 * Returns a readable type string for the specified node.
	 */
	public static String getNodeTypeString(final Node node) {
		final int type = node.getNodeType();
		switch (type) {
		case Node.ATTRIBUTE_NODE:
			return "Attribute";
		case Node.CDATA_SECTION_NODE:
			return "Cdata";
		case Node.COMMENT_NODE:
			return "Comment";
		case Node.DOCUMENT_FRAGMENT_NODE:
			return "Document Fragment";
		case Node.DOCUMENT_NODE:
			return "Document";
		case Node.DOCUMENT_TYPE_NODE:
			return "Document Type";
		case Node.ELEMENT_NODE:
			return "Element";
		case Node.ENTITY_NODE:
			return "Entity";
		case Node.ENTITY_REFERENCE_NODE:
			return "Entity Reference";
		case Node.NOTATION_NODE:
			return "Notation";
		case Node.PROCESSING_INSTRUCTION_NODE:
			return "Processing Instruction";
		case Node.TEXT_NODE:
			return "Text";
		default:
			return "Unknown";
		}
	}

	/**
	 * Gets the value of the specified node.
	 * 
	 * @param node
	 *            - The node.
	 * @return The node value.
	 */
	public static String getNodeValue(final Node node) {

		if (node == null) {
			return null;
		}
		String value = node.getNodeValue();
		if ((value != null) && (value.trim().length() > 0)) {
			return value.trim();
		}
		final NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			final Node textChild = children.item(i);
			if ((textChild != null)
					&& (textChild.getNodeType() == Node.TEXT_NODE)
					&& (textChild.getNodeValue() != null)
					&& (textChild.getNodeValue().trim().length() > 0)) {
				return textChild.getNodeValue().trim();
			} else {
				value = DOMHelper.getNodeValue(textChild);
				if (value != null) {
					return value;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the values off of each of the nodes in the specified list and
	 * returns them as a string array.
	 * 
	 * @param list
	 *            - The node list from which to get the values.
	 * @return - An array of node values.
	 */
	public static String[] getNodeValues(final NodeList list) {
		if (list != null) {
			final ArrayList<String> aList = new ArrayList<String>();
			for (int i = 0; i < list.getLength(); i++) {
				final Node node = list.item(i);
				final String value = DOMHelper.getNodeValue(node);
				if ((value != null) && (value.length() > 0)) {
					aList.add(value);
				}
			}
			final String[] strArray = new String[aList.size()];
			for (int i = 0; i < aList.size(); i++) {
				strArray[i] = aList.get(i);
			}
			return strArray;
		}
		return null;
	}

	/**
	 * Get the next non-text sibling
	 */
	public static Node getNonTextNextSibling(final Node node) {
		if (node != null) {
			Node sibling = node.getNextSibling();
			if (sibling != null) {
				// KLUDGE: Could be space - TEMP FIX: TODO:
				while ((sibling != null)
						&& (sibling.getNodeType() == Node.TEXT_NODE)) {
					sibling = sibling.getNextSibling();
				}
			}
			return sibling;
		}
		return null;
	}

	/**
	 * Get the previous non-text sibling
	 */
	public static Node getNonTextPreviousSibling(final Node node) {
		if (node != null) {
			Node sibling = node.getPreviousSibling();
			if (sibling != null) {
				// KLUDGE: Could be space - TEMP FIX: TODO:
				while ((sibling != null)
						&& (sibling.getNodeType() == Node.TEXT_NODE)) {
					sibling = sibling.getPreviousSibling();
				}
			}
			return sibling;
		}
		return null;
	}

	/**
	 * Find or create the child node and then find or create a text node for
	 * that child and set the value
	 */
	public static Node getOrCreateChildAndTextNode(final Node parent,
			final String childNodeName, final String childTextNodeValue)
			throws DOMException {
		if ((parent != null) && (childNodeName != null)
				&& (childNodeName.length() > 0)) {
			final Node childNode = DOMHelper.getOrCreateChildNode(parent,
					childNodeName);
			if ((childTextNodeValue != null) && (childNode != null)) {
				return DOMHelper.getOrCreateTextNode(childNode,
						childTextNodeValue);
			}
		}
		return null;
	}

	/**
	 * Iterate through all the elements in the node list and get or create the
	 * child and set the value of text in a textnode
	 */
	public static void getOrCreateChildAndTextNodes(final NodeList nodeList,
			final String childNodeName, final String childTextNodeValue)
			throws DOMException {
		if ((nodeList != null) && (childNodeName != null)
				&& (childNodeName.length() > 0)) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				final Node node = nodeList.item(i);
				if (node != null) {
					DOMHelper.getOrCreateChildAndTextNode(node, childNodeName,
							childTextNodeValue);
				}
			}
		}
	}

	/**
	 * Method to get the child node from a parent node If tha child node is not
	 * present it would return a new child node and append it to the parent
	 */
	public static Node getOrCreateChildNode(final Node parent, final String name)
			throws DOMException {

		final Node node = DOMHelper.getChild(parent, name);
		if (node != null) {
			return node;
		}
		// If there is no child node or something failed then create a new node
		return DOMHelper.createChildInSequence(parent, name);
	}

	/**
	 * Iterate through all the elements in the node list and get or create the
	 * child and set the value of text in a textnode
	 */
	public static void getOrCreateChildNodes(final NodeList nodeList,
			final String childNodeName) throws DOMException {
		if ((nodeList != null) && (childNodeName != null)
				&& (childNodeName.length() > 0)) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				final Node node = nodeList.item(i);
				if (node != null) {
					DOMHelper.getOrCreateChildNode(node, childNodeName);
				}
			}
		}
	}

	/**
	 * Returns a text node, creates one based on text if the given text node is
	 * not found
	 */
	public static Node getOrCreateTextNode(final Node node, final String text) {
		if (node != null) {
			if (node.hasChildNodes()) {
				node.getFirstChild().setNodeValue(text);
			} else {
				node.appendChild(node.getOwnerDocument().createTextNode(text));
			}
		}
		return node;
	}

	/**
	 * Returns a string containing the roots nodes of a list or array of
	 * documents.
	 * 
	 * @param documents
	 *            - The documents.
	 * @return The string of root node names.
	 */
	@SuppressWarnings("unchecked")
	public static String getRootNodesAsString(final Object documents) {
		if (NullChecker.isEmpty(documents)) {
			return "";
		}
		final StringBuffer docString = new StringBuffer();
		if (documents.getClass().isArray()) {
			final Object[] objects = (Object[]) documents;
			for (final Object obj : objects) {
				if (NullChecker.isEmpty(docString)) {
					docString.append(DOMHelper.getRootNodesAsString(obj));
				} else {
					docString.append(",");
					docString.append(DOMHelper.getRootNodesAsString(obj));
				}
			}
		} else if (List.class.isInstance(documents)) {
			final List documentList = (List) documents;
			for (final Object obj : documentList) {
				if (NullChecker.isEmpty(docString)) {
					docString.append(DOMHelper.getRootNodesAsString(obj));
				} else {
					docString.append(",");
					docString.append(DOMHelper.getRootNodesAsString(obj));
				}
			}
		} else if (Document.class.isInstance(documents)) {
			docString.append(((Document) documents).getDocumentElement()
					.getNodeName());
		}

		return docString.toString();
	}

	/**
	 * Get the text from a node - assume that the first child is the text node
	 */
	public static String getText(final Node node) {
		String value = "";
		if (node == null) {
			return value;
		}
		final Node child = node.getFirstChild();
		if (child == null) {
			return value;
		}
		value = child.getNodeValue();
		if (value == null) {
			value = "";
		}
		return value;
	}

	/**
	 * Get the text from a node - assume that the first child is the text node
	 */
	public static String getText(final Node parent, final String nodeName) {
		final Node node = DOMHelper.getChild(parent, nodeName);
		if (node != null) {
			final Node node_Child = node.getFirstChild();
			if (node_Child != null) {
				final String nodeValue = node_Child.getNodeValue();
				if ((nodeValue != null) && (nodeValue.length() > 0)) {
					return nodeValue;
				}
			}
		}
		return "";
	}

	/**
	 * Returns true if the specified node has a node value.
	 * 
	 * @param node
	 *            - The node.
	 * @return boolean - true or false
	 */
	public static boolean hasNodeValue(final Node node) {
		if (node != null) {
			final NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				final Node textChild = children.item(i);
				if ((textChild != null)
						&& (textChild.getNodeType() == Node.TEXT_NODE)
						&& (textChild.getNodeValue() != null)
						&& (textChild.getNodeValue().trim().length() > 0)) {
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * Method to insert after a reference node - no need to do a check for
	 * referenceNode.getNextSibling() and do a parent.appendChild(node) That
	 * above done internally Cannot insert after the last existing sibling
	 */
	public static void insertAfter(final Node parent, final Node node,
			final Node referenceNode) {
		if ((parent != null) && (node != null) && (referenceNode != null)) {
			final Node sibling = referenceNode.getNextSibling();
			if (sibling != null) {
				parent.insertBefore(node, sibling);
			} else {
				parent.appendChild(node);
			}
		}
	}

	/**
	 * Make the specified node the root node of an empty document
	 * 
	 * @param node
	 *            - The new root node.
	 */
	public static Node makeRoot(final Node node) {
		final Document doc = DOMHelper.newDocument();
		final Node aNode = doc.importNode(node, true);
		doc.appendChild(aNode);
		return aNode;
	}

	/**
	 * Make a new document root with the specified list as its children.
	 * 
	 * @param list
	 *            - The roots children.
	 * @return The children of the root document.
	 */
	public static NodeList makeRoot(final NodeList list) {
		final Document doc = DOMHelper.newDocument();
		final Element ele = doc.createElement("DummyRoot");
		doc.appendChild(ele);
		for (int i = 0; i < list.getLength(); i++) {
			final Node aNode = list.item(i);
			ele.appendChild(doc.importNode(aNode, true));
		}
		return ele.getChildNodes();
	}

	/**
	 * Make a new document root with the specified list as its children.
	 * 
	 * @param list1
	 *            - The roots children.
	 * @param list2
	 *            - The roots childern.
	 * @return NodeList - The children of the root document.
	 */
	public static NodeList makeRoot(final NodeList list1, final NodeList list2) {
		final Document doc = DOMHelper.newDocument();
		final Element ele = doc.createElement("DummyRoot");
		doc.appendChild(ele);
		for (int i = 0; i < list1.getLength(); i++) {
			final Node aNode = list1.item(i);
			ele.appendChild(doc.importNode(aNode, true));
		}
		for (int i = 0; i < list2.getLength(); i++) {
			final Node aNode = list2.item(i);
			ele.appendChild(doc.importNode(aNode, true));
		}
		return ele.getChildNodes();
	}

	/**
	 * Creates a new instance of an XML DOM document.
	 * 
	 * @return The DOM Document.
	 */
	public static Document newDocument() {
		return DOMHelper.getDocumentBuilder().newDocument();
	}

	/**
	 * Creates a new instance of an XML DOM document.
	 * 
	 * @return The DOM Document.
	 */
	public static Document newDocument(final String documentElementName) {
		final Document doc = DOMHelper.getDocumentBuilder().newDocument();
		final Element documentElement = doc.createElement(documentElementName);
		doc.appendChild(documentElement);
		return doc;
	}

	/**
	 * Imports the node into the specified document, giving it a new owner
	 * document.
	 * 
	 * @param newOwner
	 * @param node
	 * @return
	 */
	public static Node newOwner(final Document newOwner, final Node node) {
		final Node aNode = newOwner.importNode(node, true);
		return aNode;
	}

	/**
	 * Parses all the junk nodes from the specified root node.
	 * 
	 * @param node
	 *            - The root node to begin parsing.
	 * @param junkNodeList
	 *            - The list of junk nodes found.
	 * @param commentsAsJunk
	 *            - Consider comments junk nodes if set to true.
	 */
	public static void parseJunkNodes(final Node node, List<Node> junkNodeList,
			final boolean commentsAsJunk) {
		if (junkNodeList == null) {
			junkNodeList = new ArrayList<Node>();
		}
		if (NullChecker.isEmpty(node)) {
			return;
		}
		if (Node.TEXT_NODE == node.getNodeType()) {
			final String textContent = node.getNodeValue();
			if (NullChecker.isNotEmpty(textContent)
					&& NullChecker.isEmpty(textContent.trim())) {
				final Node parentNode = node.getParentNode();
				if (NullChecker.isNotEmpty(parentNode)) {
					junkNodeList.add(node);
				}
			}
		} else if ((Node.COMMENT_NODE == node.getNodeType()) && commentsAsJunk) {
			junkNodeList.add(node);
		}
		if (node.hasChildNodes()) {
			for (Node kid = node.getFirstChild(); kid != null; kid = kid
					.getNextSibling()) {
				DOMHelper.parseJunkNodes(kid, junkNodeList, commentsAsJunk);
			}
		}
	}

	/*
	 * Method to prepent child no need to do a check for parent.firstShild and
	 * do a parent.appendChild(node) That above done internally
	 */
	public static void prependChild(final Node parent, final Node node) {
		if ((parent != null) && (node != null)) {
			parent.insertBefore(node, parent.getFirstChild());
		}
	}

	/**
	 * Inserts an escape character for each character that needs escaped.
	 * 
	 * @param s
	 * @return
	 */
	public static String regexEscape(final String s) {

		final String escapeChars = "\\|[].^${}";
		final StringBuffer out = new StringBuffer();
		int i;
		char c;

		for (i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (escapeChars.indexOf(c) > -1) {
				out.append('\\');
			}
			out.append(c);
		}

		return out.toString();
	}

	/**
	 * Removes all the child nodes through recursion.
	 * 
	 * @param node
	 */
	public static void removeAll(final Node node) {
		final Node parentNode = node.getParentNode();
		if (parentNode == null) {
			DOMHelper.log.error("Node has no parent " + node.getNodeName());
			return;
		}
		if (!node.hasChildNodes()) {
			final Node sibling = node.getNextSibling();
			if (sibling != null) {
				DOMHelper.removeAll(sibling);
				final Node siblingParentNode = sibling.getParentNode();
				if (siblingParentNode != null) {
					siblingParentNode.removeChild(sibling);
				} else {
					DOMHelper.log.error("Sibling does not have parent "
							+ sibling.getNodeName());
				}
			}
			parentNode.removeChild(node);
		} else {
			// Visit the children
			final NodeList list = node.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				final Node aNode = list.item(i);
				DOMHelper.removeAll(aNode);
				final Node childSibling = aNode.getNextSibling();
				if (childSibling != null) {
					DOMHelper.removeAll(childSibling);
					final Node siblingParentNode = childSibling.getParentNode();
					if (siblingParentNode != null) {
						siblingParentNode.removeChild(childSibling);
					} else {
						DOMHelper.log
								.error("Child sibling does not have parent "
										+ childSibling.getNodeName());
					}
				}
				final Node childParentNode = aNode.getParentNode();
				if (childParentNode != null) {
					childParentNode.removeChild(aNode);
				}
			}
		}
	}

	/**
	 * Removes all child nodes of parent.
	 */
	public static void removeAllChildNodes(final Node parent) {
		if ((parent != null) && parent.hasChildNodes()) {
			// Obtaining getChildNodes() nodelist will not work
			final Node n = parent.getFirstChild();
			if (n != null) {
				parent.removeChild(n);
				DOMHelper.removeAllChildNodes(parent); // recursive call till
				// we find children
			}
		}
	}

	/*
	 * private static boolean isFilteredNode( Node node ) { return false; }
	 */
	/**
	 * Remove all text nodes that containe whitespace only.
	 */
	public static void removeEmptyTextNodes(final Node node) {

		if (!node.hasChildNodes()) {
			if ((node.getNodeType() == Node.TEXT_NODE)
					&& ((node.getNodeValue() == null) || (node.getNodeValue()
							.trim().length() == 0))) {
				// remove the text node.
				final Node parent = node.getParentNode();
				if (parent != null) {
					DOMHelper.log.info("remove node= " + node.getNodeName()
							+ " " + DOMHelper.getNodeTypeString(node) + " '"
							+ node.getNodeValue() + "' ");
					parent.removeChild(node);
				}
			}
		} else {
			final NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				DOMHelper.removeEmptyTextNodes(children.item(i));
			}
		}
	}

	/**
	 * Rename a specific Node
	 * 
	 * @param n
	 *            - the Node to rename
	 * @param doc
	 *            - the Document to search
	 * @param newName
	 *            - the String which represents new name of the Node
	 */
	public static void renameNode(final Node n, final Document doc,
			final String newName) {

		// We'd like to simply rename the node, but the
		// current API doesn't support this capability.
		// So we have to create a new element with the desired tag
		// and copy all the attributes and subordinate elements
		// into it from the original, then dispose of the original.

		Node newNode, nn;
		NamedNodeMap m;
		NodeList l;
		int i;

		newNode = doc.createElement(newName);
		m = n.getAttributes();
		for (i = 0; i < m.getLength(); i++) {
			nn = m.item(i);
			newNode.getAttributes().setNamedItem(nn.cloneNode(true));
		}
		l = n.getChildNodes();
		for (i = 0; i < l.getLength(); i++) {
			nn = l.item(i);
			newNode.appendChild(nn.cloneNode(true));
		}
		n.getParentNode().replaceChild(newNode, n);
	}

	/**
	 * Replaces the child node oldChild with newChild in the list of children,
	 * and returns the oldChild node. If newChild is a DocumentFragment object,
	 * oldChild is replaced by all of the DocumentFragment children, which are
	 * inserted in the same order. If the newChild is already in the tree, it is
	 * first removed.
	 * 
	 * @param newChild
	 *            - The new node to put in the child list.
	 * @param oldChild
	 *            - The node being replaced in the list.
	 * @return Node - the parent Node
	 */
	public static Node replaceChild(final Node newChild, final Node oldChild) {
		Node aNode = newChild;
		if (newChild.getOwnerDocument() != oldChild.getOwnerDocument()) {
			aNode = oldChild.getOwnerDocument().importNode(newChild, true);
		}
		return oldChild.getParentNode().replaceChild(aNode, oldChild);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static String toString(final Object documents) {
		if (NullChecker.isEmpty(documents)) {
			return null;
		}

		final StringBuffer docString = new StringBuffer();
		if (documents.getClass().isArray()) {
			final Object[] objects = (Object[]) documents;
			for (final Object obj : objects) {
				docString.append(DOMHelper.toString(obj));
				docString.append("\n");
			}
		} else if (List.class.isInstance(documents)) {
			final List documentList = (List) documents;
			for (final Object obj : documentList) {
				docString.append(DOMHelper.toString(obj));
				docString.append("\n");
			}
		} else if (Document.class.isInstance(documents)) {
			try {
				docString.append(DOMHelper.toString(documents));
			} catch (final DOMException ex) {
				return null;
			}
		}
		return docString.toString();

	}

	/**
	 * Updates the value of the specified node.
	 * 
	 * @param parent
	 *            - The parent element node.
	 * @param value
	 *            - The node value.
	 * @return true if the node was updated.
	 */
	public static boolean updateNodeValue(final Node parent, final Node value) {

		if ((parent == null) || (value == null)) {
			return false;
		}
		parent.appendChild(value);
		return true;
	}

	/**
	 * Updates the value of the specified node.
	 * 
	 * @param parent
	 *            - The parent element node.
	 * @param value
	 *            - The node value.
	 * @return true if the node was updated.
	 */
	public static boolean updateNodeValue(final Node parent,
			final NodeList value) {

		if ((parent == null) || (value == null)) {
			return false;
		}
		for (int i = 0; i < value.getLength(); i++) {
			parent.appendChild(value.item(i));
		}
		return true;
	}

	/**
	 * Updates the value of the specified node.
	 * 
	 * @param node
	 *            - The node.
	 * @param value
	 *            - The node value.
	 * @return true if the node was updated.
	 */
	public static boolean updateNodeValue(final Node node, final String value) {

		if (node == null) {
			return false;
		}
		boolean result = false;
		final NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			final Node textChild = children.item(i);
			if ((textChild != null)
					&& (textChild.getNodeType() == Node.TEXT_NODE)
					&& (textChild.getNodeValue() != null)
					&& (textChild.getNodeValue().trim().length() > 0)) {
				textChild.setNodeValue(value);
				return true;
			} else {
				result = DOMHelper.updateNodeValue(textChild, value);
				if (result) {
					return result;
				}
			}
		}
		return false;
	}

	/**
	 * Updates the specified values to the list of nodes.
	 * 
	 * @param list
	 *            - The list to update.
	 * @param values
	 *            - The array of values to update.
	 */
	public static void updateNodeValues(final NodeList list,
			final String[] values) {
		if (list != null) {
			for (int i = 0; i < list.getLength(); i++) {
				final Node node = list.item(i);
				DOMHelper.updateNodeValue(node, values[i]);
			}
		}
	}

}
