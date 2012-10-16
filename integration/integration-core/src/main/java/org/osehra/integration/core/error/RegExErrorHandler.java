package org.osehra.integration.core.error;

import org.osehra.integration.util.NullChecker;
import org.osehra.integration.util.RegExUtil;

import java.util.regex.Pattern;

/**
 * A regular expression error handler. Checks for error against a pattern.
 * 
 * @author Julian Jewel
 */
public class RegExErrorHandler implements ErrorHandler<String> {

	/**
	 * Error expression pattern.
	 * 
	 * @uml.property name="errorExpression"
	 */
	protected Pattern errorExpression;

	/**
	 * Throw exception on error segment.
	 * 
	 * @uml.property name="exceptionOnError"
	 */
	protected boolean exceptionOnError = true;

	@Override
	public String handleError(final String t) throws HandlerException {
		if (NullChecker.isEmpty(t)) {
			if (this.exceptionOnError) {
				throw new HandlerException("Empty data!");
			}
		}
		final String value = RegExUtil.getFirstMatchValue(t,
				this.errorExpression);
		if (NullChecker.isNotEmpty(value)) {
			if (this.exceptionOnError) {
				throw new HandlerException("Returned error: " + value);
			}
		}
		return t;
	}

	/**
	 * Set the error regular expression. TODO: Assumes a regular expression -
	 * needs to be an error handler here.
	 * 
	 * @param theErrorExpression
	 *            - the regular expression pattern
	 */
	public void setErrorExpression(final String theErrorExpression) {
		this.errorExpression = Pattern.compile(theErrorExpression,
				Pattern.DOTALL | Pattern.MULTILINE);
	}

	/**
	 * Throw a runtime exception on error.
	 * 
	 * @param theThrowExceptionOnError
	 *            - whether to throw a runtime exception on error.
	 */
	public void setRuntimeExceptionOnError(
			final boolean theThrowExceptionOnError) {
		this.exceptionOnError = theThrowExceptionOnError;
	}
}
