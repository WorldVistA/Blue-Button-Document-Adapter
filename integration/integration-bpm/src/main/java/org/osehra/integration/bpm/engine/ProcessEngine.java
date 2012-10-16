package org.osehra.integration.bpm.engine;

/**
 * The process engine interface.
 * 
 * @author Julian Jewel
 * @param < E >
 *            usually java.lang.Object
 */
public interface ProcessEngine<E> {
	/**
	 * Get the name of the process.
	 * 
	 * @return the name of the process
	 * @uml.property name="name"
	 */
	String getName();

	/**
	 * Process the request through the process.
	 * 
	 * @param arg
	 *            the input
	 * @return the output of the process, usually from the reply activity
	 * @throws ProcessException
	 *             an exception when executing the process
	 */
	E processRequest(E arg) throws ProcessException;
}
