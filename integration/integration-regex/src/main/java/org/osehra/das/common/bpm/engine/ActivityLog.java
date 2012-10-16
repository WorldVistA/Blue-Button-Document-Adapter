package org.osehra.das.common.bpm.engine;

import org.osehra.das.common.bpm.activity.Switch;
import org.osehra.das.common.bpm.activity.TransitionImpl;
import org.osehra.das.common.validation.NullChecker;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class to debug the activities. Mostly used when running in INFO log mode.
 * 
 * @author Julian Jewel
 */
public class ActivityLog implements Serializable {

	/**
	 * This class is serializable, since process context is serializable.
	 */
	private static final long serialVersionUID = -2176272737297855713L;

	/**
	 * The log elements.
	 * @uml.property  name="logElements"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private final ArrayList<String> logElements = new ArrayList<String>();

	/**
	 * The process name.
	 * @uml.property  name="processName"
	 */
	private final String processName;

	/**
	 * Constructor with process name.
	 * 
	 * @param theProcessName
	 *            the process name
	 */
	public ActivityLog(final String theProcessName) {
		this.processName = theProcessName;
	}

	/**
	 * Add a log for a print activity. This is a debug activity
	 * 
	 * @param rule
	 *            the boolean activity
	 * @param input
	 *            the input source
	 * @param outcome
	 *            the boolean outcome
	 */
	public final void add(
			final org.osehra.das.common.bpm.activity.Boolean rule,
			final Object input, final boolean outcome) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(rule.getName());
		buffer.append(" [");
		buffer.append(rule.getBeanName());
		buffer.append("]");
		buffer.append("\n|  |-- Input");
		buffer.append((NullChecker.isNotEmpty(rule.getInput()) ? "["
				+ rule.getInput() + "]:"
		/* + DOMHelper.getRootNodesAsString(input) */: ""));
		buffer.append("\n|  |-- Outcome:" + outcome);
		this.logElements.add(buffer.toString());
	}

	/**
	 * Add a log event for Switch activity.
	 * 
	 * @param rule
	 *            the switch activity
	 * @param input
	 *            the input
	 * @param outcome
	 *            the outcome string based on evaluated XPath expression
	 */
	public final void add(final Switch rule, final Object input,
			final String outcome) {
		this.logElements.add(rule.getName()
				+ " ["
				+ rule.getBeanName()
				+ "]"
				+ "\n|  |-- Input"
				+ (NullChecker.isNotEmpty(rule.getInput()) ? "["
						+ rule.getInput() + "]:"
				/* + DOMHelper.getRootNodesAsString(input) */: "")
				+ "\n|  |-- Outcome:" + outcome);
	}

	/**
	 * Add a log event for a transition activity.
	 * 
	 * @param action
	 *            the transition activity
	 * @param input
	 *            the input message
	 * @param output
	 *            the output message
	 */
	public final void add(final TransitionImpl action, final Object input,
			final Object output) {
		this.logElements.add(action.getName()
				+ " ["
				+ action.getBeanName()
				+ "]"
				+ "\n|  |-- Input"
				+ (NullChecker.isNotEmpty(action.getInput()) ? "["
						+ action.getInput() + "]:"
				/* + DOMHelper.getRootNodesAsString(input) */: "")
				+ "\n|  |-- Output"
				+ (NullChecker.isNotEmpty(action.getOutput()) ? "["
						+ action.getOutput() + "]:"
				/* + DOMHelper.getRootNodesAsString(output) */: ""));
	}

	/**
	 * Convert to string for printing.
	 * 
	 * @return the formatted string
	 */
	@Override
	public final String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("\n________________________________________________________________________________________________________________\n");
		buffer.append(this.processName);
		final Object[] activities = this.logElements.toArray();
		for (final Object activity : activities) {
			buffer.append("\n" + "|-" + activity);
		}
		buffer.append("\n________________________________________________________________________________________________________________\n");
		return buffer.toString();
	}

}
