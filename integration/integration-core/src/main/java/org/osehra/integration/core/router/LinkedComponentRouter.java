package org.osehra.integration.core.router;

import org.osehra.integration.core.component.Component;
import org.osehra.integration.core.component.ComponentException;
import org.osehra.integration.core.dispatcher.MessageDispatcher;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * The component router. It routes the message to multiple components.
 * 
 * @author Julian Jewel
 */
public class LinkedComponentRouter implements MessageDispatcher<Object, Object> {

	/**
	 * The components reference.
	 * 
	 * @uml.property name="component"
	 * @uml.associationEnd
	 */
	private List<Component<Object, Object>> components;

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
	public Object route(final Object message) throws RouterException {
		try {
			final List<Object> results = new ArrayList<Object>();
			for (final Component<Object, Object> component : this.components) {
				results.add(component.processInbound(message));
			}
			return results;
		} catch (final ComponentException ex) {
			throw new RouterException(ex);
		}
	}

	/**
	 * Set the components.
	 * 
	 * @param theComponent
	 *            the component reference.
	 */
	@Required
	public void setComponents(final List<Component<Object, Object>> components) {
		this.components = components;
	}
}
