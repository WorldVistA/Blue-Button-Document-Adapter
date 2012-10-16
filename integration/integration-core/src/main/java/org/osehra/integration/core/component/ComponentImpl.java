package org.osehra.integration.core.component;

import org.osehra.integration.core.dispatcher.MessageDispatcher;
import org.osehra.integration.core.error.ExceptionHandler;
import org.osehra.integration.core.error.HandlerException;
import org.osehra.integration.core.interceptor.Interceptor;
import org.osehra.integration.core.interceptor.InterceptorException;
import org.osehra.integration.core.receiver.MessageReceiver;
import org.osehra.integration.core.receiver.MessageReceiverException;
import org.osehra.integration.core.router.Router;
import org.osehra.integration.core.router.RouterException;
import org.osehra.integration.core.service.NamedServiceInvoker;
import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import java.text.SimpleDateFormat;
import java.util.Map;

import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedNotification;
import org.springframework.jmx.export.annotation.ManagedNotifications;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.naming.SelfNaming;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;

/**
 * The ESB component class. Any service can be wrapped up using a component
 * class. The component would intercept, transform and route the messages.
 *
 * @author Julian Jewel
 */
@ManagedResource(description = "This is the JMX management resource for monitoring DAS components.")
@ManagedNotifications(@ManagedNotification(name = "componentNotification", notificationTypes = { "componentStatistics" }))
public class ComponentImpl implements Component<Object, Object>, BeanNameAware,
		ServiceInvoker<Object, Object>, Router<Object, Object>, SelfNaming,
		NotificationPublisherAware {

	/**
	 * Failure case.
	 */
	private final static int FAILURE = 0;

	/**
	 * Failure case.
	 */
	private final static String FAILURE_STR = "FAILURE";

	/**
	 * Not initizlied case.
	 */
	private final static String NOT_INITIALIZED = "NOT_INITIALIZED";

	/**
	 * The bean has never been called.
	 */
	private final static String SUCCESS_STR = "SUCCESS";

	/**
	 * SUCCESS case.
	 */
	private final static int SUCESSS = 1;

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(ComponentImpl.class);

	/**
	 * The spring bean name - set by Spring.
	 *
	 * @uml.property name="beanName"
	 */
	private String beanName;

	/**
	 * The component's human-readable name - optional.
	 *
	 * @uml.property name="componentName"
	 */
	private String componentName = "";

	/**
	 * The component's unique ID - optional.
	 *
	 * @uml.property name="componentId"
	 */
	private String componentId = "";

	/**
	 * The exception handler.
	 *
	 * @uml.property name="exceptionHandler"
	 * @uml.associationEnd
	 */
	private ExceptionHandler<Object> exceptionHandler;

	/**
	 * Initialize Failures.
	 *
	 * @uml.property name="failures"
	 */
	protected int failures;

	/**
	 * The inbound interceptor.
	 *
	 * @uml.property name="inboundInterceptor"
	 * @uml.associationEnd
	 */
	private Interceptor<Object, Object> inboundInterceptor;

	/**
	 * The inbound router.
	 *
	 * @uml.property name="inboundRouter"
	 * @uml.associationEnd
	 */
	private Router<Object, Object> inboundRouter;

	/**
	 * The inbound transformer.
	 *
	 * @uml.property name="inboundTransformer"
	 * @uml.associationEnd
	 */
	private Transformer<Object, Object> inboundTransformer;

	/**
	 * The last known success/failure/not initialized status.
	 *
	 * @uml.property name="lastKnownState"
	 */
	protected int lastKnownState;

	/**
	 * The maximum execution time for this component.
	 *
	 * @uml.property name="maxExecutionTime"
	 */
	protected long maxExecutionTime;

	/**
	 * The message dispatcher.
	 */
	private MessageDispatcher<Object, Object> messageDispatcher;

	/**
	 * The message exchange pattern. By default its
	 * MessageExchangePattern.InOut.
	 *
	 * @uml.property name="messageExchangePattern"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private MessageExchangePattern messageExchangePattern = MessageExchangePattern.InOut;

	/**
	 * The message receiver.
	 */
	private MessageReceiver<Object, Object> messageReceiver;

	/**
	 * JMX Notification Publisher.
	 *
	 * @uml.property name="notificationPublisher"
	 * @uml.associationEnd
	 */
	private NotificationPublisher notificationPublisher;

	/**
	 * The outbound interceptor.
	 *
	 * @uml.property name="outboundInterceptor"
	 * @uml.associationEnd
	 */
	private Interceptor<Object, Object> outboundInterceptor;

	public Interceptor<Object, Object> getOutboundInterceptor() {
		return outboundInterceptor;
	}

	/**
	 * The outbound router.
	 *
	 * @uml.property name="outboundRouter"
	 * @uml.associationEnd
	 */
	private Router<Object, Object> outboundRouter;

	/**
	 * The outbound transformer.
	 *
	 * @uml.property name="outboundTransformer"
	 * @uml.associationEnd
	 */
	private Transformer<Object, Object> outboundTransformer;

	/**
	 * The service interceptor.
	 *
	 * @uml.property name="serviceInterceptor"
	 * @uml.associationEnd
	 */
	private Interceptor<Object, Object> serviceInterceptor;

	/**
	 * The service invoker.
	 *
	 * @uml.property name="serviceInvoker"
	 * @uml.associationEnd
	 */
	private ServiceInvoker<Object, Object> serviceInvoker;

	/**
	 * To enable or not to enable statistics.
	 *
	 * @uml.property name="statisticsEnabled"
	 */
	protected boolean statisticsEnabled = true;

	/**
	 * Initialize Success count variable.
	 *
	 * @uml.property name="success"
	 */
	protected int success;

	/**
	 * The last recorded timestamp.
	 *
	 * @uml.property name="updateTimeStamp"
	 */
	protected long updateTimeStamp;

	/**
	 * The error Expressions.
	 *
	 * @uml.property name="errorExpressions"
	 * @uml.associationEnd
	 */
	private static  Map<String,String> errorExpressions;




	public Map<String,String> getErrorExpressions() {
		return ComponentImpl.errorExpressions;
	}

	public void setErrorExpressions(Map<String,String> errorExpressions) {
		ComponentImpl.errorExpressions = errorExpressions;
	}

	/**
	 * Get the spring bean name.
	 *
	 * @return the bean name
	 * @uml.property name="beanName"
	 */
	public String getBeanName() {
		return this.beanName;
	}

	/**
	 * Get the component's human-readable name.
	 * This is an empty String by default.
	 *
	 * @return the componentName
	 * @uml.property name="componentName"
	 */
	public String getComponentName() {
		return componentName;
	}

	/**
	 * Get the component's unique ID.
	 * This is an empty String by default.
	 *
	 * @return the componentId
	 * @uml.property name="componentId"
	 */
	public String getComponentId() {
		return componentId;
	}

	/**
	 * Get the count of failures occured in this component.
	 *
	 * @return the failure count
	 */
	@ManagedAttribute(defaultValue = "0", description = "The count of failures occured in this component.")
	public long getFailureCount() {
		return this.failures;
	}

	/**
	 * Get the last known status of this component, whether success or failure.
	 *
	 * @return the last known status of this component
	 */
	@ManagedAttribute(defaultValue = ComponentImpl.NOT_INITIALIZED, description = "The last known execution result of the component.")
	public String getLastKnownStatus() {
		switch (this.lastKnownState) {
		case 1:
			return ComponentImpl.SUCCESS_STR;
		case 0:
			return ComponentImpl.FAILURE_STR;
		default:
			return ComponentImpl.NOT_INITIALIZED;
		}
	}

	/**
	 * Get the maximum time taken for this component to finish execution.
	 *
	 * @return the maximum execution time
	 * @uml.property name="maxExecutionTime"
	 */
	@ManagedAttribute(defaultValue = "0", description = "The longest time taken for this components to finish execution.")
	public long getMaxExecutionTime() {
		return this.maxExecutionTime;
	}

	/**
	 * Get the message exchange pattern. By default it is
	 * MessageExchangePattern.InOut.
	 *
	 * @return the message exchange pattern enumerated type
	 * @uml.property name="messageExchangePattern"
	 */
	@Override
	public MessageExchangePattern getMessageExchangePattern() {
		return this.messageExchangePattern;
	}

	@Override
	public String getName() {
		return this.beanName;
	}

	@Override
	public ObjectName getObjectName() throws MalformedObjectNameException {
		return new ObjectName(this.getClass().getName(), "type", this.beanName);
	}

	/**
	 * Get the service invoker. This is usually used by test cases to hotwire
	 * other invokers. TODO: Find another way to inject service invokers.
	 *
	 * @return the serviceInvoker
	 */
	public ServiceInvoker<Object, Object> getServiceInvoker() {
		return this.serviceInvoker;
	}

	/**
	 * Get the count of success when the component was invoked.
	 *
	 * @return the count of successes
	 */
	@ManagedAttribute(defaultValue = "0", description = "The count of successes when the component is invoked.")
	public long getSuccessCount() {
		return this.success;
	}

	/**
	 * Get the time stamp when the statistics were last updated.
	 *
	 * @return the time stamp when the component was last updated.
	 */
	@ManagedAttribute(defaultValue = ComponentImpl.NOT_INITIALIZED, description = "The recent time stamp when the component statistics were last updated.")
	public String getUpdateTimeStamp() {
		if (this.updateTimeStamp > 0) {
			final SimpleDateFormat df = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss,SSSZ");
			return df.format((new java.util.Date(this.updateTimeStamp))
					.getTime());
		}
		return ComponentImpl.NOT_INITIALIZED;
	}

	/**
	 * Intercept the message.
	 *
	 * @param interceptor
	 *            the interceptor
	 * @param arg
	 *            the input
	 * @return the return value after interceptor
	 * @throws ComponentProcessorException
	 *             an exception while intercepting the message
	 */
	protected Object intercept(
			final Interceptor<Object, Object> interceptor, final Object arg)
			throws ComponentProcessorException {
		try {
			if (NullChecker.isNotEmpty(interceptor)
					&& NullChecker.isNotEmpty(arg)) {
				if (ComponentImpl.LOG.isInfoEnabled()) {
					ComponentImpl.LOG.info(this.beanName + "-Interceptor:"
							+ interceptor.getClass().getName());
				}
				return interceptor.intercept(arg);
			}
			return arg;
		} catch (final InterceptorException ex) {
			throw new ComponentProcessorException(ex);
		}
	}

	/**
	 * Invoke the service. This is a convenience method for the ESB component to
	 * implement the ServiceInvoker interface. So bean definition
	 * serviceInvokers can directly refer to components.
	 *
	 * @param object
	 *            the input object
	 * @return the output of the component
	 * @throws ServiceInvocationException
	 *             an error occurred when processing the message through the
	 *             component
	 */
	@Override
	public Object invoke(final Object object)
			throws ServiceInvocationException {
		try {
			return this.processInbound(object);
		} catch (final ComponentException ex) {
			throw new ServiceInvocationException(ex);
		}
	}

	/**
	 * Invoke the service using the service invoker.
	 *
	 * @param invoker
	 *            the service invoker
	 * @param arg
	 *            the input
	 * @return the return value from the service
	 * @throws ComponentProcessorException
	 *             an exception occured when invoking the service through the
	 *             ServiceInvoker
	 */
	protected final Object invoke(final ServiceInvoker<Object, Object> invoker,
			final Object arg) throws ComponentProcessorException {
		try {
			if (NullChecker.isNotEmpty(invoker) && NullChecker.isNotEmpty(arg)) {
				if (ComponentImpl.LOG.isInfoEnabled()) {
					ComponentImpl.LOG.info(this.beanName + "- Splitter:"
							+ invoker.getClass().getName());
				}
				if(invoker instanceof NamedServiceInvoker) {
					((NamedServiceInvoker<Object, Object>)invoker).setComponentId(this.componentId);
					((NamedServiceInvoker<Object, Object>)invoker).setComponentName(this.componentName);
				}
				return invoker.invoke(arg);
			}
			return arg;
		} catch (final ServiceInvocationException ex) {
			throw new ComponentProcessorException(ex);
		}
	}

	/**
	 * Check whether statistics is enabled or not.
	 *
	 * @return true if statistics are enabled for this component or false
	 * @uml.property name="statisticsEnabled"
	 */
	@ManagedAttribute(defaultValue = "true", description = "True, if statistics are enabled for this component.")
	public boolean isStatisticsEnabled() {
		return this.statisticsEnabled;
	}

	/**
	 * Process inbound on the component.
	 *
	 * @param arg
	 *            the input message
	 * @return the return value of service processing the inbound message
	 * @throws ComponentException
	 *             an error occured when processing inbound message
	 */
	@Override
	public Object processInbound(final Object arg) throws ComponentException {

		int state = ComponentImpl.FAILURE;
		final long timeStamp = (new java.util.Date()).getTime();

		if (ComponentImpl.LOG.isInfoEnabled()) {
			ComponentImpl.LOG.info("Processing inbound on " + this.beanName);
		} else if (ComponentImpl.LOG.isDebugEnabled()) {
			ComponentImpl.LOG.debug("Processing inbound on " + this.beanName
					+ " Object:" + arg);
		}

		try {
			Object resultArg;
			// Received message at an Endpoint
			resultArg = this.receive(this.messageReceiver, arg);
			resultArg = this.route(this.inboundRouter, arg);
			resultArg = this.intercept(this.inboundInterceptor, resultArg);
			resultArg = this.transform(this.inboundTransformer, resultArg);
			try{
		//	System.out.println(new XMLToString().transform((Document)resultArg));
			}catch(Exception e){
				e.printStackTrace();
			}
			resultArg = this.intercept(this.serviceInterceptor, resultArg);
			resultArg = this.invoke(this.serviceInvoker, resultArg);
			// If this is an async service then stop here.
			if (!MessageExchangePattern.InOut
					.equals(this.messageExchangePattern)) {
				state = ComponentImpl.SUCESSS;
				return null;
			}
			// If this is a sync. service then process the outbound return value
			if (ComponentImpl.LOG.isInfoEnabled()) {
				ComponentImpl.LOG.info("Continuing outbound on "
						+ this.beanName);
			} else if (ComponentImpl.LOG.isDebugEnabled()) {
				ComponentImpl.LOG.debug("Continuing outbound on "
						+ this.beanName + " Object:" + arg);
			}

			resultArg = this.intercept(this.outboundInterceptor, resultArg);
			resultArg = this.route(this.outboundRouter, resultArg);
			resultArg = this.transform(this.outboundTransformer, resultArg);
			resultArg = this.route(this.messageDispatcher, resultArg);
			state = ComponentImpl.SUCESSS;
			return resultArg; // Return to the caller
		} catch (final ComponentProcessorException ex) {
			// If there is an exception handler, then handle exception
			// Handle management - TODO: Change to use AOP
			if (NullChecker.isNotEmpty(this.exceptionHandler)) {
				return this.send(arg, ex);
			} else {
				throw new ComponentException(ex);
			}
		} finally {
			if (this.isStatisticsEnabled()) {
				this.setStatistics(state, timeStamp,
						(new java.util.Date()).getTime());
			}
		}
	}

	/**
	 * Process outbound on the component.
	 *
	 * @param arg
	 *            the message outbound to the component
	 * @return the return value of processed the outbound message
	 * @throws ComponentException
	 *             an error occured when processing outbound message
	 */
	@Override
	public Object processOutbound(final Object arg)
			throws ComponentException {

		int state = ComponentImpl.FAILURE;
		final long timeStamp = (new java.util.Date()).getTime();

		if (ComponentImpl.LOG.isInfoEnabled()) {
			ComponentImpl.LOG.info("Continuing outbound on " + this.beanName);
		} else if (ComponentImpl.LOG.isDebugEnabled()) {
			ComponentImpl.LOG.debug("Continuing outbound on " + this.beanName
					+ " Object:" + arg);
		}

		try {
			Object resultArg;
			resultArg = this.intercept(this.outboundInterceptor, arg);
			resultArg = this.route(this.outboundRouter, resultArg);
			resultArg = this.transform(this.outboundTransformer, resultArg);
			resultArg = this.route(this.messageDispatcher, resultArg);
			state = ComponentImpl.SUCESSS;
			return resultArg; // Return to the caller
		} catch (final ComponentProcessorException ex) {
			if (NullChecker.isNotEmpty(this.exceptionHandler)) {
				return this.send(arg, ex);
			} else {
				throw new ComponentException(ex);
			}
		} finally {
			if (this.isStatisticsEnabled()) {
				this.setStatistics(state, timeStamp,
						(new java.util.Date()).getTime());
			}
		}
	}

	protected Object receive(
			final MessageReceiver<Object, Object> receiver, final Object arg)
			throws ComponentProcessorException {
		try {
			if (NullChecker.isNotEmpty(receiver)) {
				if (ComponentImpl.LOG.isInfoEnabled()) {
					ComponentImpl.LOG.info(this.beanName + "- Receiver:"
							+ receiver.getClass().getName());
				}
				return receiver.receive(arg);
			}
			return arg;
		} catch (final MessageReceiverException ex) {
			throw new ComponentProcessorException(ex);
		}
	}

	/**
	 * This is a convenience method for the Component itself to be a router to
	 * make the bean definitions simpler.
	 *
	 * @param arg
	 *            The input message
	 * @return the object processed by the component
	 * @throws RouterException
	 *             an error occurred when routing and processing inbound on the
	 *             component
	 */
	@Override
	public Object route(final Object arg) throws RouterException {
		try {
			return this.processInbound(arg);
		} catch (final ComponentException ex) {
			throw new RouterException(ex);
		}
	}

	/**
	 * Route an input message.
	 *
	 * @param router
	 *            the router
	 * @param arg
	 *            the input message
	 * @return the return value from the router
	 * @throws ComponentProcessorException
	 *             an exception occurred when routing the message
	 */
	protected final Object route(final Router<Object, Object> router,
			final Object arg) throws ComponentProcessorException {
		try {
			if (NullChecker.isNotEmpty(router) && NullChecker.isNotEmpty(arg)) {
				if (ComponentImpl.LOG.isInfoEnabled()) {
					ComponentImpl.LOG.info(this.beanName + "-Router:"
							+ router.getClass().getName());
				}
				return router.route(arg);
			}

			return arg;
		} catch (final RouterException ex) {
			throw new ComponentProcessorException(ex);
		}
	}

	/**
	 * Send the message to an exception handler.
	 *
	 * @param arg
	 *            the input message
	 * @param ex
	 *            the exception
	 * @return the return value from the exception handler
	 * @throws ComponentException
	 *             an error occurred when handling the exception
	 */
	public Object send(final Object arg, final Exception ex)
			throws ComponentException {
		try {
			if (NullChecker.isNotEmpty(this.exceptionHandler)) {
				return this.exceptionHandler.handleException(arg, ex, this.componentName, this.componentId);
			}
			return arg;
		} catch (final HandlerException ee) {
			throw new ComponentException(ee);
		}
	}

	/**
	 * Set the bean name. This method is internally called by Spring to set the
	 * bean name.
	 *
	 * @param theBeanName
	 *            the spring bean name
	 * @uml.property name="beanName"
	 */
	@Override
	public void setBeanName(final String theBeanName) {
		this.beanName = theBeanName;
	}

	/**
	 * Set the optional, human-readable component name.
	 *
	 * @param componentName
	 * 			the component's optional, human-readable name
	 * @uml.property name="componentName"
	 */
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	/**
	 * Set the optional, unique identifier ID for the component.
	 *
	 * @param componentId
	 * 			the component's optional, unique identifier
	 * @uml.property name="componentId"
	 */
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	/**
	 * Set the exception handler.
	 *
	 * @param theExceptionHandler
	 *            the exception handler
	 */
	public void setExceptionHandler(
			final ExceptionHandler<Object> theExceptionHandler) {
		this.exceptionHandler = theExceptionHandler;
	}

	/**
	 * Set the inbound interceptor.
	 *
	 * @param theInboundInterceptor
	 *            the inbound interceptor
	 */
	public void setInboundInterceptor(
			final Interceptor<Object, Object> theInboundInterceptor) {
		this.inboundInterceptor = theInboundInterceptor;
	}

	/**
	 * Set the inbound router.
	 *
	 * @param theInboundRouter
	 *            the inbound router
	 */
	public void setInboundRouter(
			final Router<Object, Object> theInboundRouter) {
		this.inboundRouter = theInboundRouter;
	}

	/**
	 * Set the inbound transformer.
	 *
	 * @param theInboundTransformer
	 *            the inbound transformer
	 */
	public void setInboundTransformer(
			final Transformer<Object, Object> theInboundTransformer) {
		this.inboundTransformer = theInboundTransformer;
	}

	/**
	 * Set the message dispatcher.
	 *
	 * @param theMessageDispatcher
	 *            the message dispatcher
	 */
	public void setMessageDispatcher(
			final MessageDispatcher<Object, Object> theMessageDispatcher) {
		this.messageDispatcher = theMessageDispatcher;
	}

	/**
	 * Set the message exchange pattern.
	 *
	 * @param theMessageExchangePattern
	 *            the message exchange pattern
	 * @uml.property name="messageExchangePattern"
	 */
	public void setMessageExchangePattern(
			final MessageExchangePattern theMessageExchangePattern) {
		this.messageExchangePattern = theMessageExchangePattern;
	}

	public void setMessageReceiver(
			final MessageReceiver<Object, Object> messageReceiver) {
		this.messageReceiver = messageReceiver;
	}

	/**
	 * @param theNotificationPublisher
	 * @uml.property name="notificationPublisher"
	 */
	@Override
	public void setNotificationPublisher(
			final NotificationPublisher theNotificationPublisher) {
		this.notificationPublisher = theNotificationPublisher;
	}

	/**
	 * Set the outbound interceptor.
	 *
	 * @param theOutboundInterceptor
	 *            the outbound interceptor
	 */
	public void setOutboundInterceptor(
			final Interceptor<Object, Object> theOutboundInterceptor) {
		this.outboundInterceptor = theOutboundInterceptor;
	}

	/**
	 * Set the outbound router.
	 *
	 * @param theOutboundRouter
	 *            the outbound router
	 */
	public void setOutboundRouter(
			final Router<Object, Object> theOutboundRouter) {
		this.outboundRouter = theOutboundRouter;
	}

	/**
	 * Set the outbound transformer.
	 *
	 * @param theOutboundTransformer
	 *            the outbound transformer
	 */
	public void setOutboundTransformer(
			final Transformer<Object, Object> theOutboundTransformer) {
		this.outboundTransformer = theOutboundTransformer;
	}

	/**
	 * Set the service interceptor.
	 *
	 * @param theServiceInterceptor
	 *            the service interceptor
	 */
	public void setServiceInterceptor(
			final Interceptor<Object, Object> theServiceInterceptor) {
		this.serviceInterceptor = theServiceInterceptor;
	}

	/**
	 * Set the service invoker.
	 *
	 * @param theServiceInvoker
	 *            the service invoker
	 */
	public void setServiceInvoker(
			final ServiceInvoker<Object, Object> theServiceInvoker) {
		this.serviceInvoker = theServiceInvoker;
	}

	/**
	 * Update the statistics for this bean. The method is under a synchronized
	 * block due to shared access by components.
	 *
	 * @param status
	 *            whether success or failure 1 = success, 0 = failure
	 * @param executionStartTime
	 *            the start time for execution of the component
	 * @param executionEndTime
	 *            the end time of the execution of the component
	 */
	public synchronized void setStatistics(final int status,
			final long executionStartTime, final long executionEndTime) {
		if (status < ComponentImpl.SUCESSS) {
			this.failures++;
		} else {
			this.success++;
		}
		this.lastKnownState = status;
		// Calculate execution time
		final long executionTime = executionEndTime - executionStartTime;
		// Maximum exeuction time is lesser than current execution time, then
		// set it
		if (executionTime > this.maxExecutionTime) {
			this.maxExecutionTime = executionTime;
		}
		// Set the update timestamp
		this.updateTimeStamp = (new java.util.Date()).getTime();
		// Notify clients
		if (NullChecker.isNotEmpty(this.notificationPublisher)) {
			this.notificationPublisher.sendNotification(new Notification(
					"componentStatistics", this, 0, executionTime, this
							.getLastKnownStatus()));
		}
		if (ComponentImpl.LOG.isInfoEnabled()) {
			ComponentImpl.LOG.info("Updated Component Statistics for " + "\n"
					+ this.beanName + " Last Known Status: "
					+ this.getLastKnownStatus() + "\n" + this.beanName
					+ " Execution Time: " + executionTime);
		}

	}

	/**
	 * Enable/Disable statistics.
	 *
	 * @param statisticsEnabled
	 *            true or false - true to enable statistics, false otherwise
	 * @uml.property name="statisticsEnabled"
	 */
	@ManagedOperation(description = "Enable/Disable Statistics")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "statisticsEnabled", description = "Enable/Disable Statistics") })
	public void setStatisticsEnabled(final boolean statisticsEnabled) {
		this.statisticsEnabled = statisticsEnabled;
	}

	/**
	 * Execute the transformer.
	 *
	 * @param transformer
	 *            the transformer
	 * @param arg
	 *            the input message
	 * @return the transformed output
	 * @throws ComponentProcessorException
	 *             an error occured in transformation
	 */
	protected final Object transform(
			final Transformer<Object, Object> transformer, final Object arg)
			throws ComponentProcessorException {
		try {
			if (NullChecker.isNotEmpty(transformer)
					&& NullChecker.isNotEmpty(arg)) {
				if (ComponentImpl.LOG.isInfoEnabled()) {
					ComponentImpl.LOG.info(this.beanName + "- Transformer:"
							+ transformer.getClass().getName());
				}
				return transformer.transform(arg);
			}
			return arg;
		} catch (final TransformerException ex) {
			throw new ComponentProcessorException(ex);
		}
	}
}
