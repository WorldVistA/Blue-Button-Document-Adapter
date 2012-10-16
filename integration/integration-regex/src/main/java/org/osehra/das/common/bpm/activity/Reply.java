package org.osehra.das.common.bpm.activity;

import org.osehra.das.common.bpm.engine.ActivityException;
import org.osehra.das.common.bpm.engine.ProcessContext;
import org.osehra.das.common.validation.Assert;
import org.osehra.das.common.validation.NullChecker;
import org.osehra.das.common.validator.Validator;
import org.osehra.das.common.validator.ValidatorException;

import org.springframework.beans.factory.InitializingBean;

/**
 * Reply from a process. It validates the reply if a validator is present. If
 * the input is null, the activity returns null.
 * 
 * @author Julian Jewel
 */
public class Reply extends TransitionImpl implements InitializingBean {

	/**
	 * Can reply empty?
	 * @uml.property  name="emptyReply"
	 */
	private boolean emptyReply = true;

	/**
	 * The reply message validator.
	 * @uml.property  name="validator"
	 * @uml.associationEnd  
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
	public final void afterPropertiesSet() {
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
	protected final Object executeAction(final ProcessContext context,
			final Object object) throws ActivityException {
		if (NullChecker.isEmpty(object) && this.emptyReply) {
			return null;
		} else if (NullChecker.isEmpty(object)) {
			throw new RuntimeException("Reply cannot be empty!");
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
	 * Set empty reply if reply can be empty.
	 * @param theEmptyReply  true if reply can be empty, false otherwise
	 * @uml.property  name="emptyReply"
	 */
	public final void setEmptyReply(final boolean theEmptyReply) {
		this.emptyReply = theEmptyReply;
	}

	/**
	 * Validates the input message.
	 * 
	 * @param theValidator
	 *            the validator for the message
	 */
	public final void setValidator(final Validator<Object> theValidator) {
		this.validator = theValidator;
	}
}
