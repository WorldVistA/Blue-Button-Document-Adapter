package org.osehra.das.common.bpm.activity;

import org.osehra.das.common.bpm.engine.Activity;
import org.osehra.das.common.bpm.engine.ActivityException;
import org.osehra.das.common.bpm.engine.ProcessContext;

/**
 * @author  Asha Amritraj
 */
public interface Transition {

	public void afterExecuteActivity(final ProcessContext context,
			final Object outputSource) throws ActivityException;

	public ProcessContext execute(final ProcessContext context)
			throws ActivityException;

	/**
	 * @uml.property  name="input"
	 */
	public String getInput();

	public String[] getInputs();

	/**
	 * @uml.property  name="output"
	 */
	public String getOutput();

	public Activity<ProcessContext> getTransitionTo();

	public void setTransitionTo(final Activity<ProcessContext> theTransitionTo);

}
