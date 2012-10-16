package org.osehra.das.common.atom.aggregator;

import org.osehra.das.common.aggregator.Aggregator;
import org.osehra.das.common.aggregator.AggregatorException;
import org.osehra.das.common.validation.NullChecker;

import java.util.ArrayList;
import java.util.List;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

public class FeedAggregator implements Aggregator<Object, Feed> {

	@Override
	public Feed aggregate(final Object results) throws AggregatorException {
		if (NullChecker.isEmpty(results)) {
			return null;
		}

		final List<Entry> entries = new ArrayList<Entry>();
		if (List.class.isInstance(results)) {
			for (final Object result : (List) results) {
				if (Feed.class.isInstance(result)) {
					entries.addAll(((Feed) result).getEntries());
				}
			}
		} else {
			if (Feed.class.isInstance(results)) {
				entries.addAll(((Feed) results).getEntries());
			}
		}
		// Aggregate feeds
		final Feed f = Abdera.getInstance().getFactory().newFeed();
		if (NullChecker.isNotEmpty(entries)) {
			for (final Entry entry : entries) {
				f.addEntry(entry);
			}
		}
		return f;
	}
}
