package org.osehra.integration.test.printer;

/**
 * A generic interface for printing a data value.
 * 
 * @author Keith Roberts
 * 
 * @param <V>
 */
public interface Printer<V> {

	/**
	 * Prints the specified value.
	 * 
	 * @param value
	 *            - The value to print.
	 */
	public void print(V value);

}
