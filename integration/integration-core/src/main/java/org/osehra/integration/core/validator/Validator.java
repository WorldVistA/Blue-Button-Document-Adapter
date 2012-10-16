package org.osehra.integration.core.validator;

/**
 * Interface for validating messages.
 * 
 * @author Julian Jewel
 * @param <E>
 */
public interface Validator<E> {

	/**
	 * Validate the source message. True if validation is a success, false if
	 * failure. Some validators might throw exceptions to stop processing the
	 * message due to fatal errors.
	 * 
	 * @param object
	 *            the input source
	 * @return the result true if success, false if failure
	 * @throws ValidatorException
	 *             an exception occured when validating the message.
	 */
	boolean validate(E object) throws ValidatorException;

}
