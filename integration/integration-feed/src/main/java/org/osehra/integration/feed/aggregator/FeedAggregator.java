package org.osehra.integration.feed.aggregator;

import org.osehra.integration.core.aggregator.Aggregator;
import org.osehra.integration.core.aggregator.AggregatorException;
import org.osehra.integration.feed.creator.CoreFeedCreator;
import org.osehra.integration.feed.handler.DasExtensionUtil;
import org.osehra.integration.util.NullChecker;

import java.util.ArrayList;
import java.util.List;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.springframework.beans.factory.annotation.Required;

public class FeedAggregator implements Aggregator<Object, Feed> {
	
	private String siteName;
	
	private String siteId;
	
	private CoreFeedCreator coreFeedCreator;
	
	private DasExtensionUtil dasExtensionUtil;
		
	@Override
	public Feed aggregate(final Object results) throws AggregatorException {		
		if (NullChecker.isEmpty(results)) {
			return null;
		}
		try {
			final List<Entry> entries = new ArrayList<Entry>();	
			final List<Element> extensions = new ArrayList<Element>();		
			if (List.class.isInstance(results)) {
				for (final Object result : (List<?>) results) {
					if (Feed.class.isInstance(result)) {	
						
						entries.addAll(((Feed) result).getEntries());
						
						final List<Element> tmp_extensions = new ArrayList<Element>();
						tmp_extensions.addAll(((Feed) result).getExtensions());
						
						// check to see if (any type of) extensions exist, and if so, gather them
						if (NullChecker.isNotEmpty(tmp_extensions)) {
							for (final Element element : tmp_extensions) {
								extensions.add(element);
							}
						}
	
					}
				}
			} else {
				if (Feed.class.isInstance(results)) {
					if (Feed.class.isInstance(results)) {
						entries.addAll(((Feed) results).getEntries());
						extensions.addAll(((Feed) results).getExtensions());
					}
				}
			}
			// aggregate Feed extensions and entries into one Feed		
			Feed f = this.coreFeedCreator.createFeed();
			if(NullChecker.isNotEmpty(extensions)) {			
				f = this.aggregateExtensions(f, extensions);			
			} else {
				// no dasextension was received, so add one to the new Feed
				// will add the request UriInfo here later from a Process Transform Activity
				f = this.dasExtensionUtil.addSuccessDasExtension(f, this.siteName, this.siteId, null);
			}
			if (NullChecker.isNotEmpty(entries)) {
				for (final Entry entry: entries) {
					f.addEntry(entry);
				}
			}			
			return f;
		} catch (Exception ex) {
			throw new AggregatorException(ex);
		}
	}
	
	private Feed aggregateExtensions(Feed feed, List<Element> extensions) {	
		if (NullChecker.isNotEmpty(feed)) {
			if (NullChecker.isNotEmpty(extensions)) {		
				// loop thru all extension elements, 
				// and merge the dasextension elements into feed
				for(final Element currentExt : extensions) {				
					feed = this.dasExtensionUtil.mergeDasExtension(feed, currentExt);
				}
			}
		}
		return feed;
	}


	@Required
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@Required
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	@Required
	public void setCoreFeedCreator(CoreFeedCreator coreFeedCreator) {
		this.coreFeedCreator = coreFeedCreator;
	}

	@Required
	public void setDasExtensionUtil(DasExtensionUtil dasExtensionUtil) {
		this.dasExtensionUtil = dasExtensionUtil;
	}

}
