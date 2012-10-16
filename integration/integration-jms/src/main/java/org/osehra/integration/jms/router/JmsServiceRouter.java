package org.osehra.integration.jms.router;

import org.osehra.integration.core.router.Router;
import org.osehra.integration.core.router.RouterException;
import org.osehra.integration.jms.JmsTransport;

/**
 * A router that routes messages to a service using the JMS protocol.
 * 
 * @author Julian Jewel
 */
public class JmsServiceRouter extends JmsTransport implements
		Router<Object, Object> {

	/**
	 * Route the message to a JMS queue based on the JMS template.
	 * 
	 * @param source
	 *            the input message
	 * @return the input message
	 * @throws RouterException
	 *             an exception occured when sending the message
	 */
	@Override
	public Object route(final Object source) throws RouterException {
		return this.send(source);
	}
}
