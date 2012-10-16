package org.osehra.das.common.selector.xml;

import org.osehra.das.common.selector.Selector;
import org.osehra.das.common.selector.SelectorException;
import org.osehra.das.common.validation.NullChecker;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpression;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * This selector selects a node or nodes of an XML document and returns it as
 * the specified type.
 * 
 * @author Keith Roberts
 * 
 */
public class XPathExpressionSelector implements Selector<Document, Object> {

	/**
	 * @uml.property  name="expression"
	 * @uml.associationEnd  
	 */
	private XPathExpression expression;
	/* These will be XPathConstants values */
	/**
	 * @uml.property  name="returnType"
	 * @uml.associationEnd  
	 */
	private QName returnType;

	/**
	 * Selected the value by applying the XPath expression to the source
	 * document.
	 * 
	 * @param sourceData
	 *            - The document on which to perform the selection.
	 * @return Returns the selected data. The return type will be that DOM type
	 *         specified by the returnType property.
	 */

	@Override
	public Object select(final Document sourceData) throws SelectorException {
		try {
			final Object value = this.expression.evaluate(sourceData,
					this.returnType);
			if (NullChecker.isNotEmpty(value)) {
				return value;
			}
			throw new SelectorException("Selected value is "
					+ ((value == null) ? "null" : "empty") + " for "
					+ this.expression);
		} catch (final javax.xml.xpath.XPathException ex) {
			throw new SelectorException("XPathExpression " + this.expression
					+ " failed to select value", ex);
		}
	}

	/**
	 * Sets the DOM type of class to be returned.
	 * @param type  - The DOM type. Typically a value of the XPathConstants class.  XPathContants.STRING - returns a value of class  java.lang.String. XPathConstants.BOOLEAN - returns a value of  class java.lang.Boolean. XPathConstants.NODE - returns a value  of class org.w3c.dom.Node. XPathConstants.NODESET - returns a  value of class org.w3c.dom.NodeList.
	 * @uml.property  name="returnType"
	 */
	@Required
	public void setReturnType(final QName type) {
		this.returnType = type;
	}

	/**
	 * Sets the XPath expression used for selection.
	 * 
	 * @param expression
	 *            - The XPath expression.
	 */
	@Required
	public void setXPathExpression(final XPathExpression expression) {
		this.expression = expression;
	}

}
