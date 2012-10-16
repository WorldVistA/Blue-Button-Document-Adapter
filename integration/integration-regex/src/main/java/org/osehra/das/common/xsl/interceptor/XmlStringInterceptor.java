package org.osehra.das.common.xsl.interceptor;

import org.osehra.das.common.interceptor.Interceptor;
import org.osehra.das.common.interceptor.InterceptorException;
import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.transformer.xml.StringToXML;
import org.osehra.das.common.transformer.xml.XMLToString;
import org.osehra.das.common.validation.NullChecker;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlStringInterceptor implements Interceptor<String, String> {

	/**
	 * @uml.property  name="cssReferenceMap"
	 * @uml.associationEnd  qualifier="rootNodeName:java.lang.String java.lang.String"
	 */
	Map<String, String> cssReferenceMap;
	/**
	 * @uml.property  name="stringToXml"
	 * @uml.associationEnd  
	 */
	StringToXML stringToXml;
	/**
	 * @uml.property  name="stylesheetReferenceMap"
	 * @uml.associationEnd  qualifier="rootNodeName:java.lang.String java.lang.String"
	 */
	Map<String, String> stylesheetReferenceMap;
	/**
	 * @uml.property  name="xmlToString"
	 * @uml.associationEnd  
	 */
	XMLToString xmlToString;

	@Override
	public String intercept(final String object) throws InterceptorException {
		try {
			if (NullChecker.isNotEmpty(object)) {
				final Document doc = this.stringToXml.transform(object);
				final Element root = doc.getDocumentElement();
				final String rootNodeName = root.getNodeName();
				final String xslReference = this.stylesheetReferenceMap
						.get(rootNodeName);
				if (NullChecker.isNotEmpty(xslReference)) {
					final Node pi = doc.createProcessingInstruction(
							"xml-stylesheet", "type=\"text/xsl\" href=\""
									+ xslReference + "\"");
					doc.insertBefore(pi, root);
				}
				final String cssReference = this.cssReferenceMap
						.get(rootNodeName);
				if (NullChecker.isNotEmpty(cssReference)) {
					final Node cssNode = doc.createProcessingInstruction(
							"xml-stylesheet", "type=\"text/css\" href=\""
									+ cssReference + "\"");
					doc.insertBefore(cssNode, root);
				}

				return this.xmlToString.transform(doc);
			}
			return object;
		} catch (final TransformerException ex) {
			throw new InterceptorException(ex);
		}
	}

	@Required
	public void setCssReferenceMap(final Map<String, String> cssReferenceMap) {
		this.cssReferenceMap = cssReferenceMap;
	}

	/**
	 * @param stringToXml
	 * @uml.property  name="stringToXml"
	 */
	@Required
	public void setStringToXml(final StringToXML stringToXml) {
		this.stringToXml = stringToXml;
	}

	@Required
	public void setStylesheetReferenceMap(
			final Map<String, String> stylesheetReferenceMap) {
		this.stylesheetReferenceMap = stylesheetReferenceMap;
	}

	/**
	 * @param xmlToString
	 * @uml.property  name="xmlToString"
	 */
	@Required
	public void setXmlToString(final XMLToString xmlToString) {
		this.xmlToString = xmlToString;
	}
}
