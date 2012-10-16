package org.osehra.integration.core.component;

import java.util.Map;

/**
 * The component interface. It is a wrapper to any service. The class can
 * process inbound or outbound.
 *
 * @author Julian Jewel
 * @param < T >
 *            Usually the object
 */
public interface Component<E, T> {
	/**
	 * The message exchange pattern. For Async./Sync. invocation.
	 *
	 * @return the message exchange pattern
	 * @uml.property name="messageExchangePattern"
	 * @uml.associationEnd
	 */
	MessageExchangePattern getMessageExchangePattern();

	/**
	 * The name of the component.
	 *
	 * @return
	 */
	String getName();

	/**
	 * Process the inbound message at the endpoint.
	 *
	 * @param arg
	 *            the input message
	 * @return the output after processing the inbound message
	 * @throws ComponentException
	 *             if an exception happened when processing the message
	 */
	T processInbound(E arg) throws ComponentException;

	/**
	 * Process the outbound message at the endpoint.
	 *
	 * @param arg
	 *            the input message
	 * @return the output after processing the outbound message
	 * @throws ComponentException
	 *             if an exception happened when processing the message
	 */
	T processOutbound(E arg) throws ComponentException;


	Map<String,String> getErrorExpressions() ;

}
