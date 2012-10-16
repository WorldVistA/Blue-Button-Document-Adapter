package org.osehra.das.common.router;

import org.osehra.das.common.aggregator.Aggregator;
import org.osehra.das.common.aggregator.AggregatorException;

import org.springframework.beans.factory.annotation.Required;

/**
 * The component router. It routes the message to a component.
 * 
 * @author Julian Jewel
 */
public class AggregatorRouter implements Router<Object, Object> {

	/**
	 * The component reference.
	 * @uml.property  name="aggregator"
	 * @uml.associationEnd  
	 */
	private Aggregator<Object, Object> aggregator;

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
			return this.aggregator.aggregate(message);
		} catch (final AggregatorException ex) {
			throw new RouterException(ex);
		}
	}

	@Required
	public void setAggregator(final Aggregator<Object, Object> aggregator) {
		this.aggregator = aggregator;
	}

}
