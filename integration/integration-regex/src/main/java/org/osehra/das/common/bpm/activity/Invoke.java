package org.osehra.das.common.bpm.activity;

import org.osehra.das.common.bpm.engine.ActivityException;
import org.osehra.das.common.bpm.engine.ProcessContext;
import org.osehra.das.common.bpm.engine.ProcessEngine;
import org.osehra.das.common.bpm.engine.ProcessException;
import org.osehra.das.common.bpm.persistence.PersistentContext;
import org.osehra.das.common.component.Component;
import org.osehra.das.common.component.ComponentException;
import org.osehra.das.common.component.MessageExchangePattern;
import org.osehra.das.common.validation.Assert;
import org.osehra.das.common.validation.NullChecker;
import org.osehra.das.common.validator.Validator;
import org.osehra.das.common.validator.ValidatorException;

import java.util.List;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

/**
 * Invokes an ESB component or a BPM process. It also validates the inbound and
 * outbound messages. For asynchronous components, if a message identifier
 * expression is provided, then the process context is persisted based on the
 * message identifier. It is later restored by the state resolvers when a reply
 * if received.
 * 
 * @author Julian Jewel
 */
public class Invoke extends TransitionImpl {

	/**
	 * The endpoint to send the message to.
	 * @uml.property  name="endpoint"
	 * @uml.associationEnd  
	 */
	private Component<Object, Object> endpoint;

	/**
	 * An inbound validator to validate the messages sent to the component.
	 * @uml.property  name="inboundValidator"
	 * @uml.associationEnd  
	 */
	private Validator<Object> inboundValidator;

	/**
	 * The XPath expression to extract the message identifier from the input. If an expression is specified then only one input is allowed.
	 * @uml.property  name="messageId"
	 * @uml.associationEnd  
	 */
	private XPathExpression messageId;

	/**
	 * An outbound validator to validate the outbound messages from the component.
	 * @uml.property  name="outboundValidator"
	 * @uml.associationEnd  
	 */
	private Validator<Object> outboundValidator;

	/**
	 * The persistence context to persist the process context if the message identifier is set.
	 * @uml.property  name="persistentContext"
	 * @uml.associationEnd  
	 */
	private PersistentContext persistentContext;

	/**
	 * The process to send messages to.
	 * @uml.property  name="process"
	 * @uml.associationEnd  
	 */
	private ProcessEngine<Object> process;

	/**
	 * After executing the component, call the outbound validator. This is
	 * usually called when a process is restored after an asynchronous invoke.
	 * 
	 * @param context
	 *            the process context - usually the restored context
	 * @param outputSource
	 *            the output of the invoke
	 * @throws ActivityException
	 *             an exception is validation error occurs
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final void afterExecuteActivity(final ProcessContext context,
			final Object outputSource) throws ActivityException {

		if (NullChecker.isNotEmpty(this.outboundValidator)) {
			try {
				// Temporary fix to get test cases to return array of responses
				if (List.class.isInstance(outputSource)) {
					for (final Object source : (java.util.List) outputSource) {
						this.outboundValidator.validate(source);
					}
				} else {
					this.outboundValidator.validate(outputSource);
				}
			} catch (final ValidatorException ex) {
				throw new ActivityException(ex);
			}
		}

		super.afterExecuteActivity(context, outputSource);
	}

	/**
	 * Executes the invoke activity.
	 * 
	 * @param context
	 *            the process context
	 * @param source
	 *            the input
	 * @throws ActivityException
	 *             an exception occured when invoking the component
	 * @return the output of the component
	 */
	@Override
	protected final Object executeAction(final ProcessContext context,
			final Object source) throws ActivityException {

		Assert.assertFalse(
				NullChecker.isNotEmpty(this.endpoint)
						&& NullChecker.isNotEmpty(this.process),
				"Endpoint and process cannot be defined in Invoke for bean: "
						+ this.getBeanName());

		if (NullChecker.isNotEmpty(this.inboundValidator)) {
			try {
				this.inboundValidator.validate(source);
			} catch (final ValidatorException ex) {
				throw new ActivityException(ex);
			}
		}
		// Execute component
		if (NullChecker.isNotEmpty(this.endpoint)) {
			try {
				// Store the process context
				if (NullChecker.isNotEmpty(this.messageId)) {
					Assert.assertInstance(source, Document.class);

					try {
						final String messageIdString = this.messageId
								.evaluate(source);
						Assert.assertNotEmpty(messageIdString,
								"Could not evaluate message id from source for bean: "
										+ this.getBeanName());
						this.persistentContext.put(messageIdString, context);
					} catch (final XPathExpressionException ex) {
						throw new RuntimeException(ex);
					}
				}
				// Process inbound
				return this.endpoint.processInbound(source);

			} catch (final ComponentException ex) {
				throw new ActivityException(ex);
			}
		} else if (NullChecker.isNotEmpty(this.process)) { // Execute
			// process
			try {
				return this.process.processRequest(source);
			} catch (final ProcessException ex) {
				throw new ActivityException(ex);
			}
		} else {
			throw new RuntimeException("Endpoint or RuleEngine has to be set!");
		}
	}

	/**
	 * Post execution of the activity.
	 * 
	 * @return true to continue with the invoke, false to stop execution
	 */
	@Override
	protected final boolean onPostExecute() {
		// If the component is not empty and if the component is of type
		// AsynchronousInOut, then stop execution. Since it will continue
		// execution after the reply is received.
		if (NullChecker.isNotEmpty(this.endpoint)) {
			if (MessageExchangePattern.AsynchronousInOut.equals(this.endpoint
					.getMessageExchangePattern())
					&& NullChecker.isNotEmpty(this.getOutput())) {
				return false; // Stop execution of the process
			}
		}
		// Continue execution of the process
		return true;
	}

	/**
	 * Set the component to invoke.
	 * 
	 * @param theComponent
	 *            the component
	 */
	public final void setEndpoint(final Component<Object, Object> theComponent) {
		this.endpoint = theComponent;
	}

	/**
	 * Set the inbound validator for the message to the component.
	 * 
	 * @param theInboundValidator
	 *            the inbound validator
	 */
	public final void setInboundValidator(
			final Validator<Object> theInboundValidator) {
		this.inboundValidator = theInboundValidator;
	}

	/**
	 * Set the message identifier XPath expression. This is needed to extract
	 * the message id and to persist the context on asynchronous invocation.
	 * 
	 * @param theMessageIdExpression
	 *            the message identifier expression
	 */
	public final void setMessageId(final String theMessageIdExpression) {
		try {
			this.messageId = XPathFactory.newInstance().newXPath()
					.compile(theMessageIdExpression);
		} catch (final XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * The outbound validator - to validate the message from the component.
	 * 
	 * @param theOutboundValidator
	 *            the outbound validator
	 */
	public final void setOutboundValidator(
			final Validator<Object> theOutboundValidator) {
		this.outboundValidator = theOutboundValidator;
	}

	/**
	 * Set the persistent context reference to persist the process context.
	 * @param thePersistentContext  the persistent context reference
	 * @uml.property  name="persistentContext"
	 */
	public final void setPersistentContext(
			final PersistentContext thePersistentContext) {
		this.persistentContext = thePersistentContext;
	}

	/**
	 * Set the process to invoke.
	 * 
	 * @param theProcess
	 *            the process to invoke
	 */
	public final void setProcess(final ProcessEngine<Object> theProcess) {
		this.process = theProcess;
	}
}
