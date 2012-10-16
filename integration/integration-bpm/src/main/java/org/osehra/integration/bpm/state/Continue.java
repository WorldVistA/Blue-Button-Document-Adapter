package org.osehra.integration.bpm.state;

import org.osehra.integration.bpm.ConversationPhase;
import org.osehra.integration.bpm.activity.Transition;

/**
 * Continue state.
 * 
 * @author Julian Jewel
 */
public class Continue extends AbstractState {

	/**
	 * Continue phase.
	 * 
	 * @uml.property name="conversationPhase"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final ConversationPhase conversationPhase = ConversationPhase.CONTINUE;

	/**
	 * The transition that invoked this activity and paused the process.
	 * 
	 * @uml.property name="transition"
	 * @uml.associationEnd
	 */
	private Transition transition;

	/**
	 * The conversation Phase. ConversationPhase.CONTINUE.
	 * 
	 * @return ConversationPhase.CONTINUE
	 * @uml.property name="conversationPhase"
	 */
	@Override
	public ConversationPhase getConversationPhase() {
		return this.conversationPhase;
	}

	/**
	 * Get the transition.
	 * 
	 * @return the transition
	 * @uml.property name="transition"
	 */
	public Transition getTransition() {
		return this.transition;
	}

	/**
	 * Set the activity that is needed to continue the process.
	 * 
	 * @param theTransition
	 *            the activity that is needed to continue the process
	 * @uml.property name="transition"
	 */
	public void setTransition(final Transition theTransition) {
		this.transition = theTransition;
	}
}
