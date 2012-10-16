package org.osehra.integration.http.util;


import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.core.transformer.xml.XMLToString;
import org.osehra.integration.util.NullChecker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * This class is used to build response status code based on the 
 * Atom feed error extensions; as well as 2nd-pass response error 
 * status codes.
 *
 *
 * @author parumalla, John W. May
 *
 */
public class ResponseBuilderUtil {
	
	

	private static Map<XPathExpression, String> errorExpressions;

	/**
	 * @param feedDocument
	 * @param errorExpressions
	 * 
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */	
	public static com.sun.jersey.api.client.ClientResponse.Status getResponseStatusFromFirstPassMessage(
			Object message) throws IOException, ParserConfigurationException, 
			TransformerException, SAXException,	XPathExpressionException {
		if (NullChecker.isEmpty(message)) {
			return null;
		}
		String messageStr = null;
		if (message instanceof Document) {
			messageStr = new XMLToString()
					.transform((Document) message);
		} else if (message instanceof String) {			
			messageStr = (String) message;
		} else {
			return null;
		}
		Document xmlDocument = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new InputSource(new ByteArrayInputStream(messageStr
						.getBytes("utf-8"))));
		if(NullChecker.isNotEmpty(errorExpressions)) {
			List<Integer> statusCodes = new ArrayList<Integer>();			
			for (Entry<XPathExpression, String> errorExpression : ((Map<XPathExpression, String>)errorExpressions)
					.entrySet()) {
				XPathExpression xPathErrorExpression = errorExpression.getKey();
				Boolean isErrorExpressionMatched = (Boolean) xPathErrorExpression
						.evaluate(xmlDocument, XPathConstants.BOOLEAN);
				if (isErrorExpressionMatched) {
					String errorExpressionErrorCodeValue = (errorExpressions).get(errorExpression.getKey());					
					statusCodes.add(Integer.parseInt(errorExpressionErrorCodeValue));
				}
			}
			if (statusCodes.size() > 0) {
				if (statusCodes.contains(Status.INTERNAL_SERVER_ERROR.getStatusCode())) {
					return Status.INTERNAL_SERVER_ERROR;
				} else if (statusCodes.contains(Status.NOT_FOUND.getStatusCode())
						&& statusCodes.contains(Status.OK.getStatusCode())) {
					return Status.PARTIAL_CONTENT;
				} else if (statusCodes.contains(Status.NOT_FOUND.getStatusCode())) {
					return Status.NOT_FOUND;
				} else {
					return Status.OK;
				}
			}
		}
		return Status.OK;	 
	}

	public static void setErrorExpressions(Map<String, String> errExpressions) 
			throws XPathExpressionException {
		if (NullChecker.isNotEmpty(errExpressions)) {
			errorExpressions = new HashMap<XPathExpression,String>();
			XPath xPath = XPathFactory.newInstance().newXPath();
			for (Entry<String, String> entry : ((Map<String, String>) errExpressions).entrySet()) {
				XPathExpression expression = xPath.compile(entry.getKey());
				errorExpressions.put(expression, errExpressions.get(entry.getKey()));
			}
		}
	}

}
