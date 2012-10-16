package org.osehra.das.common.router;

import org.osehra.das.common.component.Component;
import org.osehra.das.common.component.ComponentException;
import org.osehra.das.common.dispatcher.MessageDispatcher;
import org.osehra.das.common.validation.Assert;
import org.osehra.das.common.validation.NullChecker;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

/**
 * Content based router. Routes a message based on content evaluated by an XPath
 * expression to a component. The input is evaluated against a boolean XPath
 * expression and if evaluated as true, then it is routed to the component that
 * is specified in the hash table for that expression.
 * 
 * @author Julian Jewel
 */
public class ContentBasedRouter implements MessageDispatcher<Object, Object> {

	/**
	 * The routing list with the boolean xpath expressions and the component to route to.
	 * @uml.property  name="routingList"
	 * @uml.associationEnd  qualifier="expression:javax.xml.xpath.XPathExpression org.osehra.das.common.component.Component"
	 */
	private final Map<XPathExpression, Component<Object, Object>> routingList;

	/**
	 * Default constructor. Creates the routing list with the compiled XPath
	 * expressions.
	 * 
	 * @param theRoutingList
	 *            the routing list
	 */
	public ContentBasedRouter(
			final LinkedHashMap<String, Component<Object, Object>> theRoutingList) {
		this.routingList = new Hashtable<XPathExpression, Component<Object, Object>>();
		for (final java.util.Map.Entry<String, Component<Object, Object>> entry : theRoutingList
				.entrySet()) {
			final Component<Object, Object> endpoint = entry.getValue();
			final String key = entry.getKey();
			try {
				final XPathExpression expression = XPathFactory.newInstance()
						.newXPath().compile(key);
				this.routingList.put(expression, endpoint);
			} catch (final XPathExpressionException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Route the message. The message is first evaluated to an XPath expression
	 * and if true, the message is routed to the component in the map matching
	 * that expression.
	 * 
	 * @param message
	 *            the input message
	 * @return the return value from the component
	 * @throws RouterException
	 *             an exception when routing or executing the component
	 */
	@Override
	public final Object route(final Object message) throws RouterException {
		try {
			Assert.assertInstance(message, Document.class);
			for (final XPathExpression expression : this.routingList.keySet()) {
				final Boolean booleanValue = (Boolean) expression.evaluate(
						message, XPathConstants.BOOLEAN);
				if (NullChecker.isNotEmpty(booleanValue) && booleanValue) {
					final Component<Object, Object> endpoint = this.routingList
							.get(expression);
					return endpoint.processInbound(message);
				}
			}
		} catch (final ComponentException ex) {
			throw new RouterException(ex);
		} catch (final XPathExpressionException ex) {
			throw new RouterException(ex);
		}
		return null;
	}
}
