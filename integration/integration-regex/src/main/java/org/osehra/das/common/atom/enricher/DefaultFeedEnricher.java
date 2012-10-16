package org.osehra.das.common.atom.enricher;

import org.osehra.das.common.enricher.Enricher;
import org.osehra.das.common.enricher.EnricherException;
import org.osehra.das.common.validation.NullChecker;

import java.util.List;

import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.springframework.beans.factory.annotation.Required;

/**
 * Intercepts the link and updates the title, sub title, version and base URI.
 *
 * @author Asha Amritraj
 *
 */
public class DefaultFeedEnricher implements Enricher<Feed> {

	/**
	 * The Base URI of the Feed.
	 */
	String baseUri;
	/**
	 * The sub title of the feed.
	 */
	String subTitle;
	/**
	 * The title of the feed.
	 */
	String title;
	/**
	 * The version of the feed.
	 */
	String version;

	@Override
	public Feed enrich(final Feed feed) throws EnricherException {
		// Empty Feed - return NULL
		if (NullChecker.isEmpty(feed)) {
			return null;
		}
		// If a title is present, then set everything
		if (NullChecker.isNotEmpty(this.title)) {
			feed.setTitle(this.title);
		}
		if (NullChecker.isNotEmpty(this.subTitle)) {
			feed.setSubtitle(this.subTitle);
		}
		if (NullChecker.isNotEmpty(this.baseUri)) {
			feed.addLink(this.baseUri, "self");
			feed.addLink(this.baseUri, "alternate");
			feed.setBaseUri(this.baseUri);
		}
		if (NullChecker.hasEmpty(this.baseUri, this.version, this.title)) {
			feed.setGenerator(this.baseUri, this.version, this.title);
		}

		// Get all the links and update the Base URI
		for (final Entry entry : feed.getEntries()) {
			final List<Link> links = entry.getLinks();
			for (final Link link : links) {
				if (NullChecker.isNotEmpty(link)) {
					link.setBaseUri(this.baseUri);
					// TEMP FIX: For Mozilla to render the feed correctly
					// TODO: CACI - Fix Issue with Links
					link.setHref(this.baseUri + link.getHref());
				}
			}
		}
		return feed;
	}

	/**
	 * Set the Base URI - Required Field.
	 */
	@Required
	public void setBaseUri(final String baseUri) {
		this.baseUri = baseUri;
	}

	/**
	 * Set the subtitle of the feed.
	 */
	public void setSubTitle(final String subTitle) {
		this.subTitle = subTitle;
	}

	/**
	 * Set the title of the feed.
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Set the version of the feed.
	 */
	public void setVersion(final String version) {
		this.version = version;
	}
}
