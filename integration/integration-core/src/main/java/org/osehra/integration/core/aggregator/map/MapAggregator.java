package org.osehra.integration.core.aggregator.map;

import org.osehra.integration.core.aggregator.Aggregator;
import org.osehra.integration.core.aggregator.AggregatorException;
import org.osehra.integration.util.NullChecker;

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
