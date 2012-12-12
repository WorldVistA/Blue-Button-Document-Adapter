package org.osehra.das;

/**
 * Similar to the <code>java.text.Format</code> abstract class (to create a 
 * string representation of an object, or to create an object from its
 * string representation), except implementations should make their 
 * classes thread-safe.
 * 
 * @author Dept of VA
 *
 */
public interface FormatTS {
	
	/**
	 * 
	 * @param item Input item to evaluate
	 * @return String representation of an object
	 */
	String formatObject(Object item);
	
	/**
	 * 
	 * @param data Input string to evaluate
	 * @return Object generated from an input string
	 */
	Object parse(String data);
}
