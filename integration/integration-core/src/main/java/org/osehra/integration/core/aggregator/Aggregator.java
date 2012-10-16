package org.osehra.integration.core.aggregator;

public interface Aggregator<E, T> {

	T aggregate(E t) throws AggregatorException;

}
