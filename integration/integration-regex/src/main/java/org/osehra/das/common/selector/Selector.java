package org.osehra.das.common.selector;

/**
 * The generic interface whose implementors perform some sort of select from a
 * value object.
 * 
 * @author Keith Roberts
 * 
 * @param <V>
 *            - The value on which to perform selection.
 * @param <R>
 *            - The value that is selected.
 */
public interface Selector<V, R> {

	/**
	 * Selects a value from a value object.
	 * 
	 * @param value
	 *            - The value on which to perform the selection.
	 * @return Returns the value selected.
	 * @throws SelectorException
	 *             - An exception occurred durring selection.
	 */
	public R select(V value) throws SelectorException;

}
