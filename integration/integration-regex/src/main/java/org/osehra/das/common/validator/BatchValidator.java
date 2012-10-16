package org.osehra.das.common.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * Pass the message through multiple validators.
 * 
 * @author Julian Jewel
 */
public class BatchValidator implements Validator<Object> {

	/**
	 * List of validators.
	 * @uml.property  name="validators"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.osehra.das.common.validator.Validator"
	 */
	private List<Validator<Object>> validators;

	/**
	 * Set the list of validators.
	 * 
	 * @param theValidators
	 *            the validators
	 */
	@Required
	public final void setValidators(final List<Validator<Object>> theValidators) {
		this.validators = theValidators;
	}

	/**
	 * Validate the input object.
	 * 
	 * @param object
	 *            the input object
	 * @return whether true or false - false if validation is a failure
	 * @throws ValidatorException
	 *             if an error occurred in validation. Some validators might
	 *             throw exception to stop processing if the object fails
	 *             validation.
	 */
	@Override
	public final boolean validate(final Object object)
			throws ValidatorException {
		for (final Validator<Object> validator : this.validators) {
			final boolean outcome = validator.validate(object);
			if (!outcome) {
				return false;
			}
		}
		return true;
	}
}
