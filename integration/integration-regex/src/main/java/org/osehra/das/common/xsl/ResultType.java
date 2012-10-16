package org.osehra.das.common.xsl;

/**
 * Result type for transformer to decide to use DOMResult or StreamResult.
 * 
 * @author Julian Jewel
 */
public enum ResultType {
	/**
	 * DOM would use DOMResult and String would use StreamResult.
	 */
	DOM, STRING
}
