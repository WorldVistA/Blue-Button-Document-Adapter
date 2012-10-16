package org.osehra.integration.bpm.activity;

import org.osehra.integration.bpm.engine.ActivityException;
import org.osehra.integration.bpm.engine.ProcessContext;
import org.osehra.integration.core.aggregator.Aggregator;
import org.osehra.integration.core.aggregator.AggregatorException;
import org.osehra.integration.util.NullChecker;

import org.springframework.beans.factory.annotation.Required;

/**
 * Aggregate the input. Split the results if needed.
 * 
 * @author Julian Jewel
 */
public class AggregateExt extends TransitionImpl {

	Aggregator<Object, Object> aggregator;

	/**
	 * Execute the aggregate activity.
	 * 
	 * @param context
	 *            the process context
	 * @param source
	 *            the input XML or array of XML documents
	 * @return Return the aggregated document
	 * @throws ActivityException
	 *             if an error occurred on aggregation
	 */
	@Override
	protected Object executeAction(final ProcessContext context,
			final Object source) throws ActivityException {
		if (NullChecker.isEmpty(source)) {
			return source;
		}
		try {
			return this.aggregator.aggregate(source);
		} catch (final AggregatorException ex) {
			throw new ActivityException(ex);
		}
	}

	@Required
	public void setAggregator(final Aggregator<Object, Object> aggregator) {
		this.aggregator = aggregator;
	}

}
