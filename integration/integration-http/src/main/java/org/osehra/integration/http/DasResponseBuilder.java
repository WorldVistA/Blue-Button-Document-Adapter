package org.osehra.integration.http;


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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xerces.dom.DeferredDocumentImpl;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.core.spi.factory.ResponseImpl;

/**
 * This class is used to build response status code based on the atom feed error extensions
 *
 *
 * @author parumalla
 *
 */
public class DasResponseBuilder {

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
	public static javax.ws.rs.core.Response.ResponseBuilder getResponseStatus(
			Object message) throws IOException, ParserConfigurationException, 
			TransformerException, SAXException,	XPathExpressionException {
		if (NullChecker.isEmpty(message)) {
			return null;
		}
		String messageStr = null;
		if (message instanceof DeferredDocumentImpl) {
			messageStr = new XMLToString()
					.transform((DeferredDocumentImpl) message);
		} else {
			messageStr = ((ResponseImpl) (message)).getEntity().toString();
		}
		Document xmlDocument = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new InputSource(new ByteArrayInputStream(messageStr
						.getBytes("utf-8"))));
		if(NullChecker.isNotEmpty(errorExpressions)) {
			List<Integer> statusCodes = new ArrayList<Integer>();			
			for (Entry<XPathExpression, String> entry : ((Map<XPathExpression, String>)errorExpressions)
					.entrySet()) {
				XPathExpression xPathErrorExpression = entry.getKey();
				Boolean isErrorExpressionMatched = (Boolean) xPathErrorExpression
						.evaluate(xmlDocument, XPathConstants.BOOLEAN);
				if (isErrorExpressionMatched) {
					statusCodes.add(Integer.parseInt((errorExpressions).get(entry.getKey())));
				}
			}
			if (statusCodes.size() > 0) {
				if (statusCodes.contains(Status.INTERNAL_SERVER_ERROR.getStatusCode())) {
					return Response.status(Status.INTERNAL_SERVER_ERROR);
				} else if (statusCodes.contains(Status.NOT_FOUND.getStatusCode())
						&& statusCodes.contains(Status.OK.getStatusCode())) {
					return Response.status(Status.PARTIAL_CONTENT);
				} else if (statusCodes.contains(Status.NOT_FOUND.getStatusCode())) {
					return Response.status(Status.NOT_FOUND);
				}
			}
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_XML)
					.entity(messageStr);		 
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
