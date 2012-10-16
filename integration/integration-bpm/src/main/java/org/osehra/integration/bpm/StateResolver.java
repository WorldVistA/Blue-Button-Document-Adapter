package org.osehra.integration.bpm;

import org.osehra.integration.bpm.engine.ProcessContext;

/**
 * If a process needs to resolve the state its currently at, it must implement
 * the StateResolver. Based on the type of state resolver the context is
 * restored based on input.
 * 
 * @author Julian Jewel
 */
public interface StateResolver {
	/**
	 * Resolve the state of the process and restore the context if needed.
	 * 
	 * @param source
	 *            the input
	 * @return the ProcessContext, new or restored based on the state specified
	 *         in the process
	 */
	ProcessContext resolveState(final Object source);
}
