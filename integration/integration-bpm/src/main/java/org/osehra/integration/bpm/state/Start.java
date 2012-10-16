package org.osehra.integration.bpm.state;

import org.osehra.integration.bpm.ConversationPhase;
import org.osehra.integration.bpm.engine.Activity;
import org.osehra.integration.bpm.engine.ProcessContext;

/**
 * Start state.
 * 
 * @author Julian Jewel
 */
public class Start extends AbstractState {
	/**
	 * The activity to start.
	 * 
	 * @uml.property name="activity"
	 * @uml.associationEnd
	 */
	private Activity<ProcessContext> activity;
	/**
	 * The conversation phase, ConversationPhase.START.
	 * 
	 * @uml.property name="convPhase"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final ConversationPhase convPhase = ConversationPhase.START;
	/**
	 * The input variable to set.
	 * 
	 * @uml.property name="input"
	 */
	private String input;

	/**
	 * Get the activity to start.
	 * 
	 * @return the activity to start.
	 */
	public Activity<ProcessContext> getActivity() {
		return this.activity;
	}

	/**
	 * Get the conversation phase.
	 * 
	 * @return ConversationPhase.START
	 */
	@Override
	public ConversationPhase getConversationPhase() {
		return this.convPhase;
	}

	/**
	 * Get the input.
	 * 
	 * @return the input string
	 * @uml.property name="input"
	 */
	public String getInput() {
		return this.input;
	}

	/**
	 * Set the activity to start.
	 * 
	 * @param theActivity
	 *            the activity to start
	 */
	public void setActivity(final Activity<ProcessContext> theActivity) {
		this.activity = theActivity;
	}

	/**
	 * Set the input.
	 * 
	 * @param theInput
	 *            the input
	 * @uml.property name="input"
	 */
	public void setInput(final String theInput) {
		this.input = theInput;
	}
}
