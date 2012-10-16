package org.osehra.das.common.router.jms;

import org.osehra.das.common.jms.JmsTransport;
import org.osehra.das.common.router.Router;
import org.osehra.das.common.router.RouterException;

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
	public final Object route(final Object source) throws RouterException {
		return this.send(source);
	}
}
