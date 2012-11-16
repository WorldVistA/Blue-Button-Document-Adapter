package org.osehra.das;

/**
 * Similar to the Format abstract class, except implementors should 
 * make their classes thread-safe
 *
 */
public interface IFormatTS {
	String formatObject(Object item);
	Object parse(String data);
}
