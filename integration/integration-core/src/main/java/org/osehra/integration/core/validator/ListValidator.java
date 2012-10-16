package org.osehra.integration.core.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * Validates all the messages in the list.
 * 
 * @author Julian Jewel
 */
public class ListValidator implements Validator<List<Object>> {

	/**
	 * The validator to use.
	 * 
	 * @uml.property name="validator"
	 * @uml.associationEnd
	 */
	private Validator<Object> validator;

	/**
	 * Set the validator.
	 * 
	 * @param theValidator
	 *            the validator
	 */
	@Required
	public void setValidator(final Validator<Object> theValidator) {
		this.validator = theValidator;
	}

	/**
	 * Validate all the documents in the list.
	 * 
	 * @param list
	 *            the list of documents
	 * @return true if the validation succeeds or failure
	 * @throws ValidatorException
	 *             an exception occured when validating the object
	 */
	@Override
	public boolean validate(final List<Object> list)
			throws ValidatorException {
		for (final Object obj : list) {
			final boolean outcome = this.validator.validate(obj);
			if (!outcome) {
				return false;
			}
		}
		return true;
	}
}
