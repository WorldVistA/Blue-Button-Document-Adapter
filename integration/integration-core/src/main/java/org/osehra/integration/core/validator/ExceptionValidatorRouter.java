package org.osehra.integration.core.validator;

import org.osehra.integration.core.router.Router;
import org.osehra.integration.core.router.RouterException;

import org.springframework.beans.factory.annotation.Required;

public class ExceptionValidatorRouter implements Router<Object, Object> {

	boolean throwExceptionOnValidationFailure = true;
	Validator<Object> validator;

	@Override
	public Object route(final Object object) throws RouterException {
		try {
			final boolean valid = this.validator.validate(object);
			if (!valid && this.throwExceptionOnValidationFailure) {
				throw new RouterException("Validation Failed!");
			}
			return object;
		} catch (final ValidatorException ex) {
			throw new RouterException(ex);
		}
	}

	public void setThrowExceptionOnValidationFailure(
			final boolean throwExceptionOnValidationFailure) {
		this.throwExceptionOnValidationFailure = throwExceptionOnValidationFailure;
	}

	@Required
	public void setValidator(final Validator<Object> validator) {
		this.validator = validator;
	}
}
