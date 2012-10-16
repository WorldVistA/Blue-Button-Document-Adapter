package org.osehra.das.common.bpm;

/**
 * A process can contain multiple phases. <br>
 * START - The process is starting, and a new context is initialized. <br>
 * CONTINUE - An existing process is continuing from where it left off with a
 * new context. <br>
 * RESTORE - An existing process is continued from where it left of with a
 * restored context.
 * 
 * @author Julian Jewel
 */
public enum ConversationPhase {
	CONTINUE, RESTORE, /**
	 * The multiple phases of the conversation.
	 */
	START
}
