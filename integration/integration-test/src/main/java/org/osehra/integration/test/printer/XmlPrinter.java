package org.osehra.integration.test.printer;

import org.osehra.integration.test.util.xml.DOMSerializerHelper;
import org.osehra.integration.util.selector.xml.XPathExpression;
import org.osehra.integration.util.selector.xml.XPathExpressionSelector;

import java.io.PrintStream;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Prints an xml document to a print stream.
 * 
 * @author Keith Roberts
 * 
 */
public class XmlPrinter implements Printer<Document> {

	private PrintStream ps = System.out;
	private boolean doFormat = true;
	private XPathExpressionSelector selector;

	/**
	 * Prints the serialized document value to the print stream.
	 * 
	 * @param value
	 *            - The document to print.
	 */
	@Override
	public void print(final Document value) {
		try {
			if (value == null) {
				this.ps.println("Input value is null");
				return;
			}
			Element aElement = value.getDocumentElement();
			if (this.selector != null) {
				aElement = (Element) this.selector.select(value);
			}
			final String xmlStr = DOMSerializerHelper.serializeDocument(
					aElement, this.doFormat, false, false);
			this.ps.println("\n\n" + xmlStr + "\n\n");
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sets the formatting.
	 * 
	 * @param doFormat
	 *            - If true the document will be formatted.
	 */
	public void setDoFormat(final boolean doFormat) {
		this.doFormat = doFormat;
	}

	/**
	 * Sets the print stream if something other than stdout is desired.
	 * 
	 * @param ps
	 *            - The print stream in which to write.
	 */
	public void setPrintStream(final PrintStream ps) {
		this.ps = ps;
	}

	/**
	 * Sets the XPath expression used for selection.
	 * 
	 * @param expression
	 *            - The XPath expression.
	 */
	public void setXPathExpression(final XPathExpression expression) {
		this.selector = new XPathExpressionSelector();
		this.selector.setReturnType(XPathConstants.NODE);
		this.selector.setXPathExpression(expression);
	}

}
