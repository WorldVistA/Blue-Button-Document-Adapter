package org.osehra.das.common.bpm.activity;

import org.osehra.das.common.bpm.engine.AbstractActivity;
import org.osehra.das.common.bpm.engine.Activity;
import org.osehra.das.common.bpm.engine.ActivityException;
import org.osehra.das.common.bpm.engine.ProcessContext;
import org.osehra.das.common.validation.NullChecker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The abstract Transition class. Any activity which derives of this activity
 * can be transitioned or executed in a flow.
 * 
 * @author Julian Jewel
 */
public abstract class TransitionImpl extends AbstractActivity implements
		Transition {

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(Transition.class);

	/**
	 * Transition to activity.
	 * @uml.property  name="transitionTo"
	 * @uml.associationEnd  
	 */
	private Activity<ProcessContext> transitionTo;

	/**
	 * After execute of the activity.
	 * 
	 * @param context
	 *            the process context
	 * @param outputSource
	 *            the output
	 * @throws ActivityException
	 *             an error occurred
	 */
	@Override
	public/** Extensible */
	void afterExecuteActivity(final ProcessContext context,
			final Object outputSource) throws ActivityException {

		if (TransitionImpl.LOG.isInfoEnabled()) {
			context.getActivityLog().add(this,
					context.getSource(this.getInput()), outputSource);
		}
		if (NullChecker.isNotEmpty(this.getOutput())
				&& NullChecker.isNotEmpty(outputSource)) {
			context.put(this.getOutput(), outputSource);
		}

		if (NullChecker.isNotEmpty(this.transitionTo)) {
			this.transitionTo.execute(context);
		}
	}

	/**
	 * Execute the activity.
	 * 
	 * @param context
	 *            the process context
	 * @param source
	 *            the input source
	 * @return the output of the activity
	 * @throws ActivityException
	 *             an error occurred when executing the activity
	 */
	protected abstract Object executeAction(ProcessContext context,
			Object source) throws ActivityException;

	/**
	 * Execute this activity.
	 * 
	 * @param context
	 *            the process context
	 * @throws ActivityException
	 *             an error occured when executing the activity
	 */
	@Override
	public final void executeComponent(final ProcessContext context)
			throws ActivityException {

		final Object[] sources = context.getSource(this.getInput());

		Object outputSource = null;
		if (NullChecker.isNotEmpty(sources) && (sources.length > 1)) {
			outputSource = this.executeAction(context, sources);
		} else if (NullChecker.isNotEmpty(sources)) {
			outputSource = this.executeAction(context, sources[0]);
		} else {
			outputSource = this.executeAction(context, null);
		}

		if (this.onPostExecute()) {
			this.afterExecuteActivity(context, outputSource);
		}
	}

	/**
	 * Get the inputs from the context. A convenience methods for derived
	 * classes to access the inputs.
	 * 
	 * @return the input string separated as array
	 */
	@Override
	public final String[] getInputs() {
		String[] input;

		if (NullChecker.isNotEmpty(this.getInput())
				&& (this.getInput().indexOf(',') > 0)) {
			input = this.getInput().split("[\\s]*,[\\s]*");
		} else {
			input = new String[1];
			input[0] = this.getInput().trim();
		}
		return input;
	}

	/**
	 * Get the transition to activity.
	 * 
	 * @return the activity to transition to
	 */
	@Override
	public final Activity<ProcessContext> getTransitionTo() {
		return this.transitionTo;
	}

	/**
	 * Post execute can be used to stop or continue the process. Returning true
	 * will continue the process, false would stop execution of the process.
	 * This is useful when invoking asynchronous process or components.
	 * 
	 * @return by default returns true
	 */
	protected/** Extensible */
	boolean onPostExecute() {
		return true;
	}

	/**
	 * Set the activity to transition to after execution.
	 * 
	 * @param theTransitionTo
	 *            the activity to transition to
	 */
	@Override
	public final void setTransitionTo(
			final Activity<ProcessContext> theTransitionTo) {
		this.transitionTo = theTransitionTo;
	}
}
