package org.osehra.integration.bpm.state;

import org.osehra.integration.bpm.ConversationPhase;
import org.osehra.integration.bpm.engine.Activity;
import org.osehra.integration.bpm.engine.ProcessContext;

/**
 * State interface.
 * 
 * @author Julian Jewel
 */
public interface State {
	/**
	 * Get the conversation phase.
	 * 
	 * @return the conversation phase
	 * @uml.property name="conversationPhase"
	 * @uml.associationEnd
	 */
	ConversationPhase getConversationPhase();

	/**
	 * Get the exception activity.
	 * 
	 * @return the exception activity
	 */
	Activity<ProcessContext> getExceptionActivity();
}
