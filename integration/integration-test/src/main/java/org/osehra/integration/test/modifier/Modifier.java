package org.osehra.integration.test.modifier;

/**
 * A generic interface that implements various strategies for modifying data.
 * 
 * @author Keith Roberts
 * 
 * @param <V>
 */
public interface Modifier<V, R> {

	/**
	 * Performs the modification on the source data.
	 * 
	 * @param source
	 *            - The source data
	 * @throws ModifyException
	 *             - An exception occurred during the modification of the data.
	 */
	public R modify(V source) throws ModifyException;

}
