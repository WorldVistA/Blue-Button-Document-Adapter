package org.osehra.integration.core.composite;

import org.osehra.integration.core.component.Component;
import org.osehra.integration.core.component.ComponentException;
import org.osehra.integration.core.router.RouterException;
import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.splitter.Splitter;
import org.osehra.integration.core.splitter.SplitterException;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * A default composite processor.
 * 
 * @author Julian Jewel
 */
public class DefaultCompositeProcessor implements
		CompositeProcessor<Object, Object> {

	/**
	 * Add empty responses to the result flag.
	 * 
	 * @uml.property name="addEmptyResponses"
	 */
	private boolean addEmptyResponses = false;
	/**
	 * The component endpoint reference.
	 * 
	 * @uml.property name="endpoint"
	 * @uml.associationEnd
	 */
	private Component<Object, Object> endpoint;

	/**
	 * The splitter.
	 * 
	 * @uml.property name="splitter"
	 * @uml.associationEnd
	 */
	private Splitter<Object, Object> splitter;

	/**
	 * Get the endpoint.
	 * 
	 * @return the component
	 */
	@Override
	public final Component<Object, Object> getEndpoint() {
		return this.endpoint;
	}

	/**
	 * Get the splitter.
	 * 
	 * @return the splitter
	 */
	@Override
	public final Splitter<Object, Object> getSplitter() {
		return this.splitter;
	}

	/**
	 * Invoke the composite processor. The composite processor is also a service
	 * invoker and a router. Split the documents and then execute each document
	 * with the component. This is a helper interface for embedding composite
	 * processors as service invokers in bean definitions.
	 * 
	 * @param object
	 *            the input message which needs to be split
	 * @return the aggregated result of the component
	 * @throws ServiceInvocationException
	 *             an exception occured when processing the message.
	 */
	@Override
	public final Object invoke(final Object object)
			throws ServiceInvocationException {
		try {
			if (NullChecker.isEmpty(object)) {
				return object;
			}

			if (NullChecker.isNotEmpty(this.splitter)) {
				final Object objects = this.splitter.split(object);
				Assert.assertNotEmpty(objects,
						"There has to be atleast one split object!");
				final List<Object> results = new ArrayList<Object>();
				if (List.class.isInstance(objects)) {
					for (final Object obj : (List<?>) objects) {
						final Object response = this.endpoint
								.processInbound(obj);
						if (NullChecker.isNotEmpty(response)) {
							results.add(response);
						} else if (this.addEmptyResponses) {
							results.add(response);
						}
					}
				} else {
					final Object response = this.endpoint
							.processInbound(objects);
					results.add(response);
				}
				return results;
			}
			return object;
		} catch (final SplitterException ex) {
			throw new ServiceInvocationException(ex);
		} catch (final ComponentException ex) {
			throw new ServiceInvocationException(ex);
		}
	}

	/**
	 * Invoke the composite processor. The composite processor is also a service
	 * invoker and a router. Split the documents and then execute each document
	 * with the component. This is a helper interface for embedding composite
	 * processors as routers in bean definitions.
	 * 
	 * @param arg
	 *            - the input message
	 * @return the result from the component
	 * @throws RouterException
	 *             an exception occured when processing the message
	 */
	@Override
	public final Object route(final Object arg) throws RouterException {
		try {
			return this.invoke(arg);
		} catch (final ServiceInvocationException ex) {
			throw new RouterException(ex);
		}
	}

	/**
	 * Add empty responses to the result flag.
	 * 
	 * @param theAddEmptyResponses
	 *            true to add empty responses, false otherwise
	 * @uml.property name="addEmptyResponses"
	 */
	public final void setAddEmptyResponses(final boolean theAddEmptyResponses) {
		this.addEmptyResponses = theAddEmptyResponses;
	}

	/**
	 * Set the endpoint to execute.
	 * 
	 * @param theEndpoint
	 *            the component
	 */
	@Required
	public final void setEndpoint(final Component<Object, Object> theEndpoint) {
		this.endpoint = theEndpoint;
	}

	/**
	 * Set the splitter.
	 * 
	 * @param theSplitter
	 *            the splitter
	 */
	@Required
	public final void setSplitter(final Splitter<Object, Object> theSplitter) {
		this.splitter = theSplitter;
	}
}
