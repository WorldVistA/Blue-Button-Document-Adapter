package org.osehra.das.common.bpm.persistence;

import org.osehra.das.common.bpm.engine.ProcessContext;

/**
 * The persistent context interface.
 * 
 * @author Julian Jewel
 */
public interface PersistentContext {
	/**
	 * Get the process context.
	 * 
	 * @param messageId
	 *            the message identifier
	 * @return the process context from the database
	 */
	ProcessContext get(String messageId);

	/**
	 * Persist the process context.
	 * 
	 * @param messageId
	 *            the message identifier
	 * @param context
	 *            the process context to be stored
	 */
	void put(String messageId, ProcessContext context);
}
