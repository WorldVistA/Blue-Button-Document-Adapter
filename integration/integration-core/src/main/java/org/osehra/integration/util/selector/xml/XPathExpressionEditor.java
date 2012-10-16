package org.osehra.integration.util.selector.xml;

import java.beans.PropertyEditorSupport;

import javax.xml.xpath.XPathException;

/**
 * A property editor used for converting a String XPath expression
 * into an XPathExpression object.
 * This is used by the Spring container for converting string
 * property values into an instance of XPathExpression.
 * @author Keith Roberts
 *
 */
public class XPathExpressionEditor extends PropertyEditorSupport {

	private XPathExpression expression;

	@Override
	public void setAsText(String text) {
		this.expression = new XPathExpression();
		if (text != null) {
			try
			{
				this.expression.compile(text);
			} catch (XPathException ex) {
				throw new RuntimeException(ex);
			}
			setValue(this.expression);
		}
	}

	@Override
	public String getAsText() {
		return this.expression.toString();
	}
}
