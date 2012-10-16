package org.osehra.integration.bpm.router;

import org.osehra.integration.bpm.engine.ProcessEngine;
import org.osehra.integration.bpm.engine.ProcessException;
import org.osehra.integration.core.router.Router;
import org.osehra.integration.core.router.RouterException;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import java.util.Hashtable;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Content based router. Evaluates the document against a XPath expression and
 * routes it to the appropriate process.
 * 
 * @author Julian Jewel
 */
public class ContentBasedRouter implements Router<Object, Object> {

	/**
	 * Routing list with boolean XPath expression and process.
	 * 
	 * @uml.property name="routingList"
	 * @uml.associationEnd qualifier=
	 *                     "expression:javax.xml.xpath.XPathExpression org.osehra.das.common.bpm.engine.ProcessEngine"
	 */
	private final Map<XPathExpression, ProcessEngine<Object>> routingList;

	/**
	 * Content based router constructor.
	 * 
	 * @param theRoutingList
	 *            the routing list with boolean XPath expression as string and
	 *            process
	 */
	public ContentBasedRouter(
			final Map<String, ProcessEngine<Object>> theRoutingList) {
		this.routingList = new Hashtable<XPathExpression, ProcessEngine<Object>>();
		for (final java.util.Map.Entry<String, ProcessEngine<Object>> entry : theRoutingList
				.entrySet()) {
			// Process
			final ProcessEngine<Object> ruleEngine = entry.getValue();
			final String key = entry.getKey();
			try {
				final XPathExpression expression = XPathFactory.newInstance()
						.newXPath().compile(key);
				this.routingList.put(expression, ruleEngine);
			} catch (final XPathExpressionException e) {
				throw new RuntimeException(e);
			}

		}
	}

	/**
	 * Route the message to the process. Evaluate the XPath expression on the
	 * document and if true, then route to the appropriate process.
	 * 
	 * @param message
	 *            the input message
	 * @throws RouterException
	 *             an error occurred when routing the message
	 * @return the result from the process
	 */
	@Override
	public Object route(final Object message) throws RouterException {
		try {
			for (final XPathExpression expression : this.routingList.keySet()) {
				final Boolean booleanValue = (Boolean) expression.evaluate(
						message, XPathConstants.BOOLEAN);
				if (NullChecker.isNotEmpty(booleanValue) && booleanValue) {
					final ProcessEngine<Object> ruleEngine = this.routingList
							.get(expression);
					Assert.assertNotEmpty(ruleEngine,
							"Process cannot be empty!");
					return ruleEngine.processRequest(message);
				}
			}
		} catch (final ProcessException ex) {
			throw new RouterException(ex);
		} catch (final XPathExpressionException ex) {
			throw new RouterException(ex);
		}
		return null;
	}
}
