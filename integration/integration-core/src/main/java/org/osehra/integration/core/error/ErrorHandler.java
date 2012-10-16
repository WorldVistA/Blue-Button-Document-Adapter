package org.osehra.integration.core.error;

/**
 * The error handler when an error needs to be handled in the payload.
 * 
 * @author Julian Jewel
 * @param <T>
 *            Usually java.lang.Object
 */
public interface ErrorHandler<T> {
	/**
	 * Handle the error.
	 * 
	 * @param t
	 *            the input object
	 * @return the return value after processing
	 * @throws HandlerException
	 *             if an error occurred in handling the payload
	 */
	T handleError(T t) throws HandlerException;
}
