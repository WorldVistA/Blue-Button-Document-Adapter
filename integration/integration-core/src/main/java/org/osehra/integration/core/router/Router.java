package org.osehra.integration.core.router;

/**
 * The router. The Message Router differs from the most basic notion of Pipes
 * and Filters in that it connects to multiple output channels. Thanks to the
 * Pipes and Filters architecture the components surrounding the Message Router
 * are completely unaware of the existence of a Message Router. A key property
 * of the Message Router is that it does not modify the message contents. It
 * only concerns itself with the destination of the message.
 * 
 * @author Julian Jewel
 * @param <E>
 *            Usually java.lang.Object
 */
public interface Router<E, T> {

	/**
	 * Route the message.
	 * 
	 * @param arg
	 *            the input message
	 * @return the value returned by the component/service routed to
	 * @throws RouterException
	 *             an error occurred when routing the message
	 */
	T route(E arg) throws RouterException;
}
