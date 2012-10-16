package org.osehra.integration.bpm.state;

import org.osehra.integration.bpm.engine.Activity;
import org.osehra.integration.bpm.engine.ProcessContext;

/**
 * Abstract State. All the states contain exception activities.
 * 
 * @author Julian Jewel
 */
public abstract class AbstractState implements State {

	/**
	 * The exception activity.
	 * 
	 * @uml.property name="exceptionActivity"
	 * @uml.associationEnd
	 */
	private Activity<ProcessContext> exceptionActivity;

	/**
	 * Get the exception activity.
	 * 
	 * @return the exception activity
	 */
	@Override
	public Activity<ProcessContext> getExceptionActivity() {
		return this.exceptionActivity;
	}

	/**
	 * Set the exception activity.
	 * 
	 * @param theExceptionActivity
	 *            the exception activity
	 */
	public void setExceptionActivity(
			final Activity<ProcessContext> theExceptionActivity) {
		this.exceptionActivity = theExceptionActivity;
	}
}
