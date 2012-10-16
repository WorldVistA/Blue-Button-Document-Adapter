package org.osehra.das.common.router;

import org.osehra.das.common.component.Component;
import org.osehra.das.common.component.ComponentException;
import org.osehra.das.common.dispatcher.MessageDispatcher;

/**
 * The component router. It routes the message to a component.
 * 
 * @author Julian Jewel
 */
public class ComponentRouter implements MessageDispatcher<Object, Object> {

	/**
	 * The component reference.
	 * @uml.property  name="component"
	 * @uml.associationEnd  
	 */
	private Component<Object, Object> component;

	/**
	 * Route the message to the component.
	 * 
	 * @param message
	 *            the message
	 * @return the output after the component has processed the message
	 * @throws RouterException
	 *             if an error occurred when routing the message
	 */
	@Override
	public final Object route(final Object message) throws RouterException {
		try {
			return this.component.processInbound(message);
		} catch (final ComponentException ex) {
			throw new RouterException(ex);
		}
	}

	/**
	 * Set the component.
	 * 
	 * @param theComponent
	 *            the component reference.
	 */
	public final void setEndpoint(final Component<Object, Object> theComponent) {
		this.component = theComponent;
	}

}
