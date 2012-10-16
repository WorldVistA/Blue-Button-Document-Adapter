package org.osehra.das.common.aggregator.map;

import org.osehra.das.common.aggregator.Aggregator;
import org.osehra.das.common.aggregator.AggregatorException;
import org.osehra.das.common.validation.NullChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapAggregator implements
		Aggregator<List<Map<String, ?>>, Map<String, Object>> {

	@Override
	public Map<String, Object> aggregate(final List<Map<String, ?>> list)
			throws AggregatorException {
		if (NullChecker.isEmpty(list)) {
			return null;
		}

		final Map<String, Object> newMap = new HashMap<String, Object>();
		// Will overwrite elements that are in list based on the order received
		for (final Map<String, ?> map : list) {
			newMap.putAll(map);
		}

		return newMap;

	}
}
