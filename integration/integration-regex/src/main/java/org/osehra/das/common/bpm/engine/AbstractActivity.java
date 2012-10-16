package org.osehra.das.common.bpm.engine;

import org.osehra.das.common.validation.Assert;
import org.osehra.das.common.validation.NullChecker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Required;

/**
 * The abstract activity. Every activity would derive of the abstract activity.
 * 
 * @author Julian Jewel
 */
public abstract class AbstractActivity implements Activity<ProcessContext>,
		BeanNameAware {
	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(AbstractActivity.class);

	/**
	 * Spring specific configuration.
	 * @uml.property  name="beanName"
	 */
	private String beanName;
	/**
	 * The input parameters which will be pulled from the process context.
	 * @uml.property  name="input"
	 * @uml.associationEnd  
	 */
	private String input;
	/**
	 * The name of the activity.
	 * @uml.property  name="name"
	 */
	private String name;
	/**
	 * The output parameter to set in the process context.
	 * @uml.property  name="output"
	 * @uml.associationEnd  
	 */
	private String output;

	/**
	 * Execute the activity.
	 * 
	 * @param context
	 *            the process context which has the inputs
	 * @return the process context updated with the output
	 * @throws ActivityException
	 *             an error occured when executing the activity
	 */
	@Override
	public final ProcessContext execute(final ProcessContext context)
			throws ActivityException {
		Assert.assertNotEmpty(context, "Context cannot be null for bean: "
				+ this.getBeanName());
		if (AbstractActivity.LOG.isInfoEnabled()) {
			AbstractActivity.LOG.info("Executing activity "
					+ this.getClass().getSimpleName() + "[" + this.beanName
					+ "]" + " with input reference: " + this.input);
		}
		if (AbstractActivity.LOG.isDebugEnabled()) {
			final Object inputObj = context.get(this.input);
			if (NullChecker.isNotEmpty(inputObj)) {
				// AbstractActivity.LOG.debug(DOMHelper.toString(inputObj));
			}
		}
		context.setCurrentActivityName(this.beanName);
		this.executeComponent(context);
		return context;
	}

	/**
	 * Execute the underlying implementation of this activity.
	 * 
	 * @param context
	 *            the process context
	 * @throws ActivityException
	 *             an error occurred when executing the process
	 */
	protected abstract void executeComponent(ProcessContext context)
			throws ActivityException;

	/**
	 * Get the spring bean name.
	 * @return  the spring bean name
	 * @uml.property  name="beanName"
	 */
	public final String getBeanName() {
		return this.beanName;
	}

	/**
	 * Get the input string.
	 * @return  the input string
	 * @uml.property  name="input"
	 */
	public final String getInput() {
		return this.input;
	}

	/**
	 * The detailed name of the activity.
	 * @return  the name of the activity
	 * @uml.property  name="name"
	 */
	@Override
	public final String getName() {
		return this.name;
	}

	/**
	 * The ouput variable name.
	 * @return  the output variable name
	 * @uml.property  name="output"
	 */
	public final String getOutput() {
		return this.output;
	}

	/**
	 * This method is used by Spring to set the bean name.
	 * @param theBeanName  the name of the spring bean
	 * @uml.property  name="beanName"
	 */
	@Override
	public final void setBeanName(final String theBeanName) {
		this.beanName = theBeanName;
	}

	/**
	 * The input variable name. It must be in the process context hashtable.
	 * @param theInput  the input variable name.
	 * @uml.property  name="input"
	 */
	public final void setInput(final String theInput) {
		this.input = theInput;
	}

	/**
	 * Set the name of the activity.
	 * @param theName  the detailed name.
	 * @uml.property  name="name"
	 */
	@Required
	public final void setName(final String theName) {
		this.name = theName;
	}

	/**
	 * Set the output variable name ot be put into the context. If the output exists in the process context, then it will be overwritten.
	 * @param theOutput  the output variable name
	 * @uml.property  name="output"
	 */
	public final void setOutput(final String theOutput) {
		this.output = theOutput;
	}
}
