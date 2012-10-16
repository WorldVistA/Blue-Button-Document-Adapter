package org.osehra.integration.core.error;

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
	
	/**
	 * Handle the exception.
	 * 
	 * @param t
	 *            the input object
	 * @param ex
	 *            the exception
	 * @param componentName
	 * 			  the throwing Component's human-readable name - may be empty
	 * @param componentId
	 * 			  the throwing Component's unique ID - may be empty
	 * @return the return value of the exception handler
	 * @throws HandlerException
	 *             if an error occurred in handling the exception
	 */
	T handleException(T t, Exception ex, String componentName, String componentId) throws HandlerException;
}
