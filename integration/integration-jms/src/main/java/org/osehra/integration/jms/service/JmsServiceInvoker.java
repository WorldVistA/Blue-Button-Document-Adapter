package org.osehra.integration.jms.service;

import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.jms.JmsTransport;

import javax.jms.Destination;

/**
 * The JMS Service Invoker. It derives from JmsTransport and is used to send
 * messages to a queue.
 * 
 * @author Julian Jewel
 */
public class JmsServiceInvoker extends JmsTransport implements
		ServiceInvoker<Object, Object> {

	/**
	 * @uml.property name="destination"
	 * @uml.associationEnd
	 */
	private Destination destination;

	/**
	 * Invoke the JMS based service by sending the object to the queue.
	 * 
	 * @param object
	 *            the input message
	 * @return the input message
	 * @throws ServiceInvocationException
	 *             an exception occured when sending the message
	 */
	@Override
	public Object invoke(final Object object)
			throws ServiceInvocationException {
		if (this.destination != null) {
			return this.send(this.destination, object);
		} else {
			return this.send(object);
		}
	}

	/**
	 * @param destination
	 * @uml.property name="destination"
	 */
	public void setDestination(final Destination destination) {
		this.destination = destination;
	}

}
