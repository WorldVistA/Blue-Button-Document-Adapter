package org.osehra.integration.util.selector.xml;

import org.osehra.integration.util.NullChecker;
import org.osehra.integration.util.selector.Selector;
import org.osehra.integration.util.selector.SelectorException;

import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * A selector that selects the first value object in a map, in which the XPath
 * expression evaluates to a true.
 * 
 * @author Keith Roberts
 * 
 */
public class MappedXPathExpressionSelector<R> implements Selector<Document, R> {

	/**
	 * @uml.property name="expressions"
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="true"
	 *                     elementType="java.lang.Object" qualifier=
	 *                     "expression:javax.xml.xpath.XPathExpression java.lang.Object"
	 */
	private Map<XPathExpression, R> expressions;

	/**
	 * Selects the first map entry whose XPath expression evaluates to true and
	 * returns the associated value object in the map. If more than one entry
	 * evaluates to true, the entry whose object is return is determined by the
	 * Map implementation.
	 * 
	 * @param sourceData
	 *            - The data on which the selection is performed.
	 * @return Returns the first map value object in which the XPath expression
	 *         evaluates to true.
	 */
	@Override
	public R select(final Document sourceData) throws SelectorException {
		try {
			for (final XPathExpression expression : this.expressions.keySet()) {
				final java.lang.Boolean booleanValue = (java.lang.Boolean) expression
						.evaluate(sourceData, XPathConstants.BOOLEAN);
				if (NullChecker.isNotEmpty(booleanValue) && booleanValue) {
					final R result = this.expressions.get(expression);
					return result;
				}
			}
			throw new SelectorException("No selectable value in doc for "
					+ this.expressions);
		} catch (final javax.xml.xpath.XPathException ex) {
			throw new SelectorException("Failed select value by XPathExpress",
					ex);
		}

	}

	/**
	 * Sets the XPath expression map.
	 * 
	 * @param expressions
	 *            - The map contains an XPath expression as a key and some
	 *            object as a value.
	 */
	@Required
	public void setXPathExpressionTargets(
			final Map<XPathExpression, R> expressions) {
		this.expressions = expressions;
	}
}
