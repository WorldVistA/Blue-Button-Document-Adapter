package org.osehra.integration.test.asserter;

/**
 * Abstract interface for applying assertion.
 * 
 * @author Keith Roberts
 * 
 * @param <V>
 */
public interface Asserter<V> {

	/**
	 * Apply the assertion if the value fails the assertion test.
	 * 
	 * @param value
	 */
	public void assertTrue(V value);

}
