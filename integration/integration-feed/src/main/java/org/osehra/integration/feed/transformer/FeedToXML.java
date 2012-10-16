package org.osehra.integration.feed.transformer;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.core.transformer.xml.StringToXML;

import org.apache.abdera.model.Feed;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

public class FeedToXML implements Transformer<Feed, Document> {
	
	Transformer<Feed, String> feedToString;
	Transformer<String, Document>  stringToXml;

	@Override
	public Document transform(final Feed feed) throws TransformerException {
		try {				
			String feedString = this.feedToString.transform(feed);				
			return this.stringToXml.transform(feedString);
		} catch (Exception ex) {
			throw new TransformerException(ex);
		}
	}

	@Required
	public void setFeedToString(Transformer<Feed, String> feedToString) {
		this.feedToString = feedToString;
	}

	@Required
	public void setStringToXml(StringToXML stringToXml) {
		this.stringToXml = stringToXml;
	}

}
