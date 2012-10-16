package org.osehra.integration.feed.enricher;

import org.osehra.integration.core.enricher.Enricher;
import org.osehra.integration.core.enricher.EnricherException;
import org.osehra.integration.util.NullChecker;

import java.util.List;
import java.util.Map;

import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

public class SummaryEnricher implements Enricher<Feed> {

	/**
	 * @uml.property name="imageLinks"
	 * @uml.associationEnd 
	 *                     qualifier="fileType:java.lang.String java.lang.String"
	 */
	Map<String, String> imageLinks;

	@Override
	public Feed enrich(final Feed feed) throws EnricherException {
		if (NullChecker.isEmpty(feed)) {
			return null;
		}

		try {
			for (final Entry entry : feed.getEntries()) {
				final List<Link> links = entry.getLinks();
				String summaryLinks = null;
				for (final Link link : links) {
					if (NullChecker.isNotEmpty(link)) {
						String relativeHref = link.getHref().toString();
						if (NullChecker.isNotEmpty(relativeHref)) {
							relativeHref = relativeHref.substring(relativeHref
									.lastIndexOf("/") + 1);
							String title = "";
							if (NullChecker.isNotEmpty(link.getTitle())) {
								title = link.getTitle() + ": ";
								// raw ampersand will cause links not to 
								// show up in summary below, as summaryFormat 
								// will not be set below into XHTML - JWM
								title = title.replaceAll("&(?!(amp;|quot;))", "&amp;");
							}
	
							String imgHref = "";
							if (NullChecker.isNotEmpty(this.imageLinks)) {
								final String fileType = relativeHref
										.substring(relativeHref.lastIndexOf(".") + 1);
								imgHref = this.imageLinks.get(fileType);
								if (NullChecker.isNotEmpty(imgHref)) {
									imgHref = "<img cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"vertical-align:bottom;\" align=\"bottom\" src=\""
											+ imgHref + "\"/>";
								}
							}
	
							final String relLink = "<tr><td align=\"right\">"
									+ title + "</td>" + "<td>" + imgHref
									+ "<a href=\"" + link.getHref() + "\">"
									+ relativeHref + "</a></td></tr>";
							if (NullChecker.isNotEmpty(summaryLinks)) {
								summaryLinks += relLink;
							} else {
								summaryLinks = relLink;
							}
						}
					}
				}
	
				String summary = entry.getSummary();
				if (NullChecker.isNotEmpty(summaryLinks)) {
					final String summaryFormat = "<table style=\"border-width: 1 px 1px 1px 1px; border-color:#600;\">"
							+ summaryLinks + "</table>";
					if (NullChecker.isEmpty(summary)) {
						entry.setSummaryAsXhtml(summaryFormat);
					} else {
						summary = summary + summaryFormat;
						entry.setSummaryAsHtml(summary);
					}
				}
	
			}
			return feed;
		} catch (Exception ex) {
			throw new EnricherException(ex);
		}
	}

	public void setImageLinks(final Map<String, String> imageLinks) {
		this.imageLinks = imageLinks;
	}
}
