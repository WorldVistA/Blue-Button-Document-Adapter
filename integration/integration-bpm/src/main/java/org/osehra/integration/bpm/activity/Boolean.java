package org.osehra.integration.bpm.activity;

import org.osehra.integration.bpm.engine.AbstractActivity;
import org.osehra.integration.bpm.engine.Activity;
import org.osehra.integration.bpm.engine.ActivityException;
import org.osehra.integration.bpm.engine.ProcessContext;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

/**
 * A boolean activity which manages the flow based on an XPath expression to a
 * success or failure transitions. Note: this activity returns false on empty
 * input.
 * 
 * @author Julian Jewel
 */
public class Boolean extends AbstractActivity {

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(Boolean.class);

	/**
	 * XPath expression to determine whether its a failure or success. Has to be
	 * a boolean expression.
	 * 
	 * @uml.property name="expression"
	 * @uml.associationEnd
	 */
	private XPathExpression expression;
	/**
	 * Failure activity.
	 * 
	 * @uml.property name="failure"
	 * @uml.associationEnd
	 */
	private Activity<ProcessContext> failure;

	/**
	 * Success activity.
	 * 
	 * @uml.property name="success"
	 * @uml.associationEnd
	 */
	private Activity<ProcessContext> success;

	/**
	 * The method is called to decide the success or failure transition based on
	 * input.
	 * 
	 * @param source
	 *            the input source
	 * @param context
	 *            the process context
	 * @return Boolean true or false
	 * @throws ActivityException
	 *             if an error occurred when processing the XPath
	 */
	protected final java.lang.Boolean decide(final Object source,
			final ProcessContext context) throws ActivityException {
		try {
			// Returns false on empty
			if (NullChecker.isEmpty(source)) {
				return false;
			}
			Assert.assertInstance(source, Document.class);
			return (java.lang.Boolean) this.expression.evaluate(source,
					XPathConstants.BOOLEAN);
		} catch (final XPathExpressionException ex) {
			throw new ActivityException(ex);
		}
	}

	/**
	 * Execute the activity with the context. Get the inputs from the context,
	 * execute the activities and put the return value in the context based on
	 * the output name.
	 * 
	 * @param context
	 *            the process context
	 * @throws ActivityException
	 *             exception if an error occured
	 */
	@Override
	protected final void executeComponent(final ProcessContext context)
			throws ActivityException {
		final Object[] sources = context.getSource(this.getInput());
		// Sometime we check for empty documents
		// Assert.assertNonEmptyArray(sources, "Expected an input!");
		Assert.assertFalse(sources.length > 1,
				"Boolean rule activity can have only one input for bean: "
						+ this.getBeanName() + " input: " + this.getInput());

		final Object source = sources[0];
		// Decide
		final java.lang.Boolean outcome = this.decide(source, context);
		// Add to activity log
		if (Boolean.LOG.isInfoEnabled()) {
			context.getActivityLog().add(this, sources, outcome);
		}
		if (NullChecker.isNotEmpty(outcome) && outcome) {
			this.success.execute(context); // Success
		} else {
			this.failure.execute(context); // Failure
		}
	}

	/**
	 * Set the XPath expression to evaluate, must be a boolean expression.
	 * 
	 * @param theExpression
	 *            the XPath expression
	 */
	public final void setExpression(final String theExpression) {

		try {
			this.expression = XPathFactory.newInstance().newXPath()
					.compile(theExpression);
		} catch (final XPathExpressionException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Set the failure activity.
	 * 
	 * @param theFailure
	 *            the failure activity.
	 */
	public void setFailure(final Activity<ProcessContext> theFailure) {
		this.failure = theFailure;
	}

	/**
	 * Set the success activity.
	 * 
	 * @param theSuccess
	 *            the success activity
	 */
	public void setSuccess(final Activity<ProcessContext> theSuccess) {
		this.success = theSuccess;
	}
}
