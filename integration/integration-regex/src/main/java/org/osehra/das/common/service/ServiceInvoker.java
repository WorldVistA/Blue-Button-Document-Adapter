package org.osehra.das.common.service;

/**
 * The service invoker interface. This is a wrapper to the service and hides the
 * protocol that is used to invoke the service, like POJO, JMS, EJB, TCP/IP etc.
 * 
 * @author Julian Jewel
 * @param <T>
 * @param <E>
 */
public interface ServiceInvoker<E, T> {

	/**
	 * Invoke the service.
	 * 
	 * @param object
	 *            the input message
	 * @return the output from the service
	 * @throws ServiceInvocationException
	 *             an error occured when invoking the service
	 */
	T invoke(E object) throws ServiceInvocationException;

}
