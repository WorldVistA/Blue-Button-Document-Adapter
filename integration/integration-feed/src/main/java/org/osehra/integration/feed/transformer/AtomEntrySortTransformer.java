package org.osehra.integration.feed.transformer;

import java.util.Comparator;

import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;

public class AtomEntrySortTransformer implements Transformer<Feed, Feed> {

	private Comparator<Entry> entryComparator = null;

	@Override
	public Feed transform(Feed src) throws TransformerException {
		if (entryComparator == null) {
			return src.sortEntriesByUpdated(true);
		}
		return src.sortEntries(entryComparator);
	}

	public void setEntryComparator(Comparator<Entry> entryComparator) {
		this.entryComparator = entryComparator;
	}

}
