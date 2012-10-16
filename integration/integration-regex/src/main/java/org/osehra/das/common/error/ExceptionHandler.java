package org.osehra.das.common.error;

/**
 * The exception handler when an exception happens invoking a component.
 * 
 * @author Julian Jewel
 * @param <T>
 *            Usually java.lang.Object
 */
public interface ExceptionHandler<T> {
	/**
	 * Handle the exception.
	 * 
	 * @param t
	 *            the input object
	 * @param ex
	 *            the exception
	 * @return the return value of the exception handler
	 * @throws HandlerException
	 *             if an error occurred in handling the exception
	 */
	T handleException(T t, Exception ex) throws HandlerException;
}
