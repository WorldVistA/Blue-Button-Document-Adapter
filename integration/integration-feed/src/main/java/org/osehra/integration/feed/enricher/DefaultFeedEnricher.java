package org.osehra.integration.feed.enricher;

import org.osehra.integration.core.enricher.Enricher;
import org.osehra.integration.core.enricher.EnricherException;
import org.osehra.integration.util.NullChecker;

import org.apache.abdera.model.Feed;

/**
 * Intercepts and enriches the message as type Feed.
 * 
 * @author Asha Amritraj
 * 
 */
public class DefaultFeedEnricher implements Enricher<Feed> {

	@Override
	public Feed enrich(Feed feed) throws EnricherException {
		// Empty Feed - return NULL
		if (NullChecker.isEmpty(feed)) {
			return null;
		}
		
		try {
			// do something to improve the Feed contents
			
			return feed;
		} catch (Exception ex) {
			throw new EnricherException(ex);
		}
	}

}
