package org.osehra.das.common.service.jms;

import org.osehra.das.common.jms.JmsTransport;
import org.osehra.das.common.service.ServiceInvocationException;
import org.osehra.das.common.service.ServiceInvoker;

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
	 * @uml.property  name="destination"
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
	public final Object invoke(final Object object)
			throws ServiceInvocationException {
		if (this.destination != null) {
			return this.send(this.destination, object);
		} else {
			return this.send(object);
		}
	}

	/**
	 * @param destination
	 * @uml.property  name="destination"
	 */
	public void setDestination(final Destination destination) {
		this.destination = destination;
	}

}
