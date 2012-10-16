package org.osehra.das.common.bpm.state;

import org.osehra.das.common.bpm.ConversationPhase;
import org.osehra.das.common.bpm.engine.Activity;
import org.osehra.das.common.bpm.engine.ProcessContext;

/**
 * State interface.
 * @author  Julian Jewel
 */
public interface State {
	/**
	 * Get the conversation phase.
	 * @return  the conversation phase
	 * @uml.property  name="conversationPhase"
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
