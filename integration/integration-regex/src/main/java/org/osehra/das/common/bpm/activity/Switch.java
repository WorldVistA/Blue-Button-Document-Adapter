package org.osehra.das.common.bpm.activity;

import org.osehra.das.common.bpm.engine.AbstractActivity;
import org.osehra.das.common.bpm.engine.Activity;
import org.osehra.das.common.bpm.engine.ActivityException;
import org.osehra.das.common.bpm.engine.ProcessContext;
import org.osehra.das.common.validation.Assert;
import org.osehra.das.common.validation.NullChecker;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * A switch activity, to switch by an XPath expression.
 *
 * @author Julian Jewel
 */
public class Switch extends AbstractActivity {

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(Switch.class);

	/**
	 * The activities and their XPathExpression's.
	 * @uml.property  name="states"
	 */
	private Map<XPathExpression, Activity<ProcessContext>> states;

	/**
	 * Decide based on XPathExpression on which activity to execute.
	 *
	 * @param source
	 *            the input
	 * @param context
	 *            the process context
	 * @return the activity which evaluates to the XPath expression
	 * @throws ActivityException
	 *             an error occurred
	 */
	protected final Activity<ProcessContext> decide(final Object source,
			final ProcessContext context) throws ActivityException {

		// Sample the first message and switch based on that
		if (List.class.isInstance(source)) {
			for (final Object splitSource : (java.util.List<?>) source) {
				return this.decide(splitSource, context);
			}
		}

		Assert.assertInstance(source, Document.class);
		// Evaluate expressions, and get the activity
		try {
			for (final XPathExpression expression : this.states.keySet()) {
				final java.lang.Boolean booleanValue = (java.lang.Boolean) expression
						.evaluate(source, XPathConstants.BOOLEAN);
				if (NullChecker.isNotEmpty(booleanValue) && booleanValue) {
					final Activity<ProcessContext> activity = this.states
							.get(expression);
					Assert.assertNotEmpty(
							activity,
							"Activity cannot be null for bean: "
									+ this.getBeanName());
					return activity;
				}
			}
		} catch (final XPathExpressionException ee) {
			throw new ActivityException(ee);
		}
		throw new RuntimeException(
				"The input did not evaluate to any expression!");

	}

	/**
	 * Execute the switch activity.
	 *
	 * @param context
	 *            the process context
	 * @throws ActivityException
	 *             the exception
	 */
	@Override
	protected final void executeComponent(final ProcessContext context)
			throws ActivityException {
		final Object[] sources = context.getSource(this.getInput());

		Assert.assertNonEmptyArray(sources, "Source cannot be empty for bean: "
				+ this.getBeanName() + " input: " + this.getInput());
		Assert.assertFalse(sources.length > 1,
				"Boolean rule activity bean " + this.getBeanName()
						+ " can have only one input. Got multiple for input: "
						+ this.getInput());

		final Object source = sources[0];

		final Activity<ProcessContext> outcome = this.decide(source, context);

		Assert.assertNotEmpty(outcome,
				"Unknown outcome for bean: " + this.getBeanName() + " input: "
						+ this.getInput());
		// Add to activity log
		if (Switch.LOG.isInfoEnabled()) {
			context.getActivityLog().add(this, sources, outcome.getName());
		}
		outcome.execute(context);
	}

	/**
	 * Set the states map with the XPath expression as the key and the activity
	 * reference as the value.
	 *
	 * @param theStates
	 *            the XPath expression, Activity pair
	 */
	@Required
	public final void setStates(
			final LinkedHashMap<String, Activity<ProcessContext>> theStates) {
		this.states = new LinkedHashMap<XPathExpression, Activity<ProcessContext>>();
		for (final java.util.Map.Entry<String, Activity<ProcessContext>> entry : theStates
				.entrySet()) {
			final Activity<ProcessContext> activity = entry.getValue();
			final String key = entry.getKey();
			Assert.assertNotEmpty(activity,
					"Activity cannot be empty for bean: " + this.getBeanName());
			try {
				final XPathExpression ex = XPathFactory.newInstance()
						.newXPath().compile(key);
				this.states.put(ex, activity);
			} catch (final XPathExpressionException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
