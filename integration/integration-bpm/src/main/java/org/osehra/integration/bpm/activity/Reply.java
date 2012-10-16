package org.osehra.integration.bpm.activity;

import org.osehra.integration.bpm.engine.ActivityException;
import org.osehra.integration.bpm.engine.ProcessContext;
import org.osehra.integration.core.validator.Validator;
import org.osehra.integration.core.validator.ValidatorException;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import org.springframework.beans.factory.InitializingBean;

/**
 * Reply from a process. It validates the reply if a validator is present. If
 * the input is null, the activity returns null.
 * 
 * @author Julian Jewel
 */
public class Reply extends TransitionImpl implements InitializingBean {

	/**
	 * The reply message validator.
	 * 
	 */
	private Validator<Object> validator;

	/**
	 * After properties are set, this method is called by spring. This method is
	 * used to validate whether the output and transition are not set for this
	 * activity.
	 * 
	 * @throws Exception
	 *             throws exception is there is an error
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.assertEmpty(this.getOutput(),
				"Reply activity bean " + this.getBeanName()
						+ " cannot have an output");
		Assert.assertEmpty(this.getTransitionTo(), "Reply activity bean "
				+ this.getBeanName() + " cannot transition");
	}

	/**
	 * Execute the reply activity.
	 * 
	 * @param context
	 *            the process context
	 * @param object
	 *            the input object
	 * @return the reply object
	 * @throws ActivityException
	 *             if an error occurred
	 */
	@Override
	protected Object executeAction(final ProcessContext context,
			final Object object) throws ActivityException {
		if (NullChecker.isEmpty(object)) {
			return null;
		}
		if (NullChecker.isNotEmpty(this.validator)) {
			try {
				this.validator.validate(object);
			} catch (final ValidatorException ex) {
				throw new ActivityException(ex);
			}
		}
		return object;
	}

	/**
	 * Validates the input message.
	 * 
	 * @param theValidator
	 *            the validator for the message
	 */
	public void setValidator(final Validator<Object> theValidator) {
		this.validator = theValidator;
	}
}
