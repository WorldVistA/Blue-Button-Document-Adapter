package org.osehra.integration.bpm.engine;

import org.osehra.integration.bpm.activity.Reply;
import org.osehra.integration.bpm.activity.Transition;
import org.osehra.integration.bpm.persistence.PersistentContext;
import org.osehra.integration.bpm.state.Continue;
import org.osehra.integration.bpm.state.Persisted;
import org.osehra.integration.bpm.state.Start;
import org.osehra.integration.bpm.state.State;
import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * Process engine implementation. The engine is defined with a state resolver.
 * The state resolver can have Start, Continue or Persisted states. The state is
 * determined by the source document. An XPathExpression is evaluated against
 * the document to determine the state. If the expressions evaluates to the
 * Start state then the input object is set to the input variable specified in
 * the state and put in the context. If the expression evaluates to the Continue
 * state then the input object is set to the output variable of the activity
 * that is to be continued. If the expression evaluates to the Persisted state,
 * then the input object is set as the output variable of the activity is to be
 * continued. Example - Start State, XPath expression="/ZCH_Z01"
 * inputVar="adcRequest" Continue State, XPath expression ="/ZCH_Z02"
 * activity="sendRequestToDOD" The sendRequestToDOD output variable will be set
 * with the ZCH_Z02.
 * 
 * @author Julian Jewel
 */
public class ProcessEngineImpl extends AbstractRuleEngine<Object> implements
		BeanFactoryAware, ServiceInvoker<Object, Object> {

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(ProcessEngineImpl.class);
	/**
	 * Bean factory reference to be populated by Spring.
	 * 
	 * @uml.property name="beanFactory"
	 * @uml.associationEnd
	 */
	private BeanFactory beanFactory;
	/**
	 * Persistence Context reference to restore the process context by
	 * correlation id.
	 * 
	 * @uml.property name="persistentContext"
	 * @uml.associationEnd
	 */
	private PersistentContext persistentContext;
	private Map<XPathExpression, State> stateResolvers;

	/**
	 * Continue an existing process.
	 * 
	 * @param state
	 *            Continue state
	 * @param source
	 *            the input
	 * @return the result from the process
	 * @throws ActivityException
	 *             an exception occurred in executing the process
	 */
	public Object continueProcess(final Continue state,
			final Object source) throws ActivityException {
		Assert.assertNotEmpty(state, "State cannot be empty!");
		final ProcessContext context = new ProcessContext(this.getName());
		final Transition transition = state.getTransition();
		transition.afterExecuteActivity(context, source);
		if (ProcessEngineImpl.LOG.isInfoEnabled()) {
			ProcessEngineImpl.LOG.info(context.getActivityLog());
		}
		return this.processReply(context);
	}

	/**
	 * Invoke the process using the service invoker interface.
	 * 
	 * @param object
	 *            the input object
	 * @return the return value from the process
	 * @throws ServiceInvocationException
	 *             an exception when invoking the service
	 */
	@Override
	public Object invoke(final Object object)
			throws ServiceInvocationException {
		try {
			return this.processRequest(object);
		} catch (final ProcessException ex) {
			throw new ServiceInvocationException(ex);
		}
	}

	/**
	 * Process the exception if an exception occurs in the process. If an
	 * exception activity is defined then the activity is executed.
	 * 
	 * @param state
	 *            Start or Continue states
	 * @param source
	 *            Input source
	 * @param ex
	 *            the Exception
	 * @return the return value from the exception process
	 * @throws ProcessException
	 *             if an exception occurred when processing the exception
	 */
	public Object processException(final State state,
			final Object source, final Exception ex) throws ProcessException {
		try {
			// Get the exception activity
			if (NullChecker.isNotEmpty(state.getExceptionActivity())) {
				ProcessContext context = new ProcessContext(this.getName());
				context.put("exception", ex);
				// If it's a start state, then set the input variable specified.
				if (Start.class.isInstance(state)) {
					context.put(((Start) state).getInput(), source);
				} else if (Continue.class.isInstance(state)) {
					// Continue an existing process - get the activity that
					// paused this process and continue that activity.
					// Put a new entry in the context, with the output variable
					// of that activity and the source object.
					final Transition transition = ((Continue) state)
							.getTransition();
					Assert.assertNotEmpty(transition,
							"Transition is required to continue the process!");
					context.put(transition.getOutput(), source);
				}
				// Execute the exception activity
				context = state.getExceptionActivity().execute(context);

				if (ProcessEngineImpl.LOG.isInfoEnabled()) {
					ProcessEngineImpl.LOG.info("Exception: " + ex.getMessage());
					ProcessEngineImpl.LOG.info(context.getActivityLog());
				}
				// Process the reply.
				return this.processReply(context);

			} else {
				// Throw exception if there is no exception activity //
				// assigned.
				throw new ProcessException(ex);
			}
		} catch (final ActivityException ee) {
			throw new ProcessException(ee);
		}
	}

	/**
	 * Process the reply from a process. Gets the input from the reply activity
	 * and returns all the input objects.
	 * 
	 * @param context
	 *            the process context
	 * @return The values in the input variables of the reply activity.
	 */
	public Object processReply(final ProcessContext context) {
		Assert.assertNotEmpty(context, "Context cannot be empty!");
		final AbstractActivity activity = (AbstractActivity) this.beanFactory
				.getBean(context.getCurrentActivityName());
		if (Reply.class.isInstance(activity)) {
			Assert.assertNotEmpty(activity,
					"Activity cannot be empty for reply activity name: "
							+ context.getCurrentActivityName());
			Assert.assertNotEmpty(
					activity.getInput(),
					"Input cannot be empty for reply activity name: "
							+ context.getCurrentActivityName());
			final Object[] sources = context.getSource(activity.getInput());
			if (sources == null) {
				return null;
			}
			if (activity.getInput().indexOf(',') > 0) {
				return sources;
			} else {
				return sources[0]; // The first element
			}

		} else { // If there is no reply activity then return null.
			return null;
		}

	}

	/**
	 * Process the request. This is the entry point to process an input message.
	 * 
	 * @param source
	 *            the input message
	 * @return the output of the process
	 * @throws ProcessException
	 *             an exception when processing the message
	 */
	@Override
	public Object processRequest(final Object source)
			throws ProcessException {
		try {
			Assert.assertInstance(source, Document.class);
			for (final Map.Entry<XPathExpression, State> entry : this.stateResolvers
					.entrySet()) {
				final XPathExpression expression = entry.getKey();
				final Boolean result = (Boolean) expression.evaluate(source,
						XPathConstants.BOOLEAN);
				if (NullChecker.isNotEmpty(result) && result) {
					final State state = entry.getValue();
					Assert.assertNotEmpty(state, "State cannot be empty");
					try {
						switch (state.getConversationPhase()) {
						case CONTINUE:
							return this.continueProcess((Continue) state,
									source);
						case START:
							return this.startProcess((Start) state, source);
						case RESTORE:
							return this.restoreProcess((Persisted) state,
									source);
						default:
							throw new RuntimeException("Unknown state!");
						}
					} catch (final ActivityException ex) {
						// Execute the exception activity if the process threw
						// an exception.
						return this.processException(state, source, ex);
					}
				}
			}
			throw new RuntimeException(
					"No expressions in state resovlers evaluated the source message");
		} catch (final XPathExpressionException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Restore the process from the persisted state.
	 * 
	 * @param state
	 *            the persisted state
	 * @param source
	 *            the input message
	 * @return the return value of the restored process
	 * @throws ActivityException
	 *             an exception occurred when restoring the process
	 */
	public Object restoreProcess(final Persisted state,
			final Object source) throws ActivityException {
		Assert.assertNotEmpty(state, "Persisted state cannot be null!");
		// Get the correlation identifier
		String correlationId;
		try {
			correlationId = (String) state.getCorrelationResolver().evaluate(
					source, XPathConstants.STRING);
		} catch (final XPathExpressionException ex) {
			throw new RuntimeException(ex);
		}

		// Restore the process context
		final ProcessContext context = this.persistentContext
				.get(correlationId);

		Assert.assertNotEmpty(context,
				"Could not find Process Context for correlation id: "
						+ correlationId);
		// Get the current activity name
		// final String activityName = context.getCurrentActivityName();
		// Assert.assertNotEmpty(activityName,
		// "Activity name cannot be null! ");
		final Transition transition = state.getTransition();
		// (Transition) this.beanFactory.getBean(activityName);
		Assert.assertNotEmpty(transition, "Transition activity cannot be null");
		// Call the after execute activity since this is a continue operation
		transition.afterExecuteActivity(context, source);
		if (ProcessEngineImpl.LOG.isInfoEnabled()) {
			ProcessEngineImpl.LOG.info(context.getActivityLog());
		}
		// Process the reply after execution of the process
		return this.processReply(context);
	}

	/**
	 * Set the bean factory. This method is executed by the Spring container to
	 * set the bean factory.
	 * 
	 * @param theBeanFactory
	 *            the bean factory will be set by Spring
	 * @uml.property name="beanFactory"
	 */
	@Override
	public void setBeanFactory(final BeanFactory theBeanFactory) {
		this.beanFactory = theBeanFactory;
	}

	/**
	 * Set the persistence context reference.
	 * 
	 * @param thePersistentContext
	 *            the persistent context reference
	 * @uml.property name="persistentContext"
	 */
	public void setPersistentContext(
			final PersistentContext thePersistentContext) {
		this.persistentContext = thePersistentContext;
	}

	/**
	 * Set the state resolvers. The Start, Continue and Restore states will be
	 * set.
	 * 
	 * @param theStateResolvers
	 *            the state resolvers with XPathExpression as the key and the
	 *            State
	 */
	@Required
	public void setStateResolvers(
			final Map<String, State> theStateResolvers) {
		this.stateResolvers = new LinkedHashMap<XPathExpression, State>();
		for (final Map.Entry<String, State> entry : theStateResolvers
				.entrySet()) {
			try {
				final XPathExpression expression = XPathFactory.newInstance()
						.newXPath().compile(entry.getKey());
				this.stateResolvers.put(expression, entry.getValue());
			} catch (final XPathExpressionException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Start the process.
	 * 
	 * @param state
	 *            the start state
	 * @param source
	 *            the input message
	 * @return the return value of the process if it has completed
	 * @throws ActivityException
	 *             an error occurred when starting the process
	 */
	public Object startProcess(final Start state, final Object source)
			throws ActivityException {
		Assert.assertNotEmpty(state, "Start state cannot be empty!");
		ProcessContext context = new ProcessContext(this.getName());
		context.put(state.getInput(), source);
		context = state.getActivity().execute(context);

		if (ProcessEngineImpl.LOG.isInfoEnabled()) {
			ProcessEngineImpl.LOG.info(context.getActivityLog());
		}
		// Reply
		return this.processReply(context);
	}
}
