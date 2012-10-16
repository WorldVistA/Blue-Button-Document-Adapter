package org.osehra.das.common.aggregator;

public interface Aggregator<E, T> {

	T aggregate(E t) throws AggregatorException;

}
