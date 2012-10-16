package org.osehra.integration.bpm.activity;

import org.osehra.integration.bpm.engine.Activity;
import org.osehra.integration.bpm.engine.ActivityException;
import org.osehra.integration.bpm.engine.ProcessContext;

/**
 * @author Asha Amritraj
 */
public interface Transition {

	public void afterExecuteActivity(final ProcessContext context,
			final Object outputSource) throws ActivityException;

	public ProcessContext execute(final ProcessContext context)
			throws ActivityException;

	/**
	 * @uml.property name="input"
	 */
	public String getInput();

	public String[] getInputs();

	/**
	 * @uml.property name="output"
	 */
	public String getOutput();

	public Activity<ProcessContext> getTransitionTo();

	public void setTransitionTo(final Activity<ProcessContext> theTransitionTo);

}
