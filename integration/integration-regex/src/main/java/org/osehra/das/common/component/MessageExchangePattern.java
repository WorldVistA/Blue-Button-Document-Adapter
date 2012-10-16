package org.osehra.das.common.component;

/**
 * Message exchange patterns. Whether the component is InOut, InOnly,
 * AsynchronousInOut or AsynchronousInOnly.
 * 
 * @author Julian Jewel
 */
public enum MessageExchangePattern {
	AsynchronousInOnly, AsynchronousInOut, InOnly, /**
	 * The various message
	 * exchange patterns.
	 */
	InOut
}
