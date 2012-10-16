package org.osehra.integration.feed.transformer;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.EscapeChars;
import org.osehra.integration.util.NullChecker;

import java.util.regex.PatternSyntaxException;

import org.apache.abdera.model.Feed;

public class FeedToString implements Transformer<Feed, String> {

	@Override
	public String transform(final Feed feed) throws TransformerException {
		try {
			if (NullChecker.isNotEmpty(feed)) {
				// TODO: Cleanup - Fix this using DOM or research why XMLNS is being
				// added to entry baseURI
				String feedString = feed.toString();
				feedString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						+ feedString;
				feedString = feedString
						.replaceAll(
								EscapeChars
										.forRegex("xmlns:xml=\"http://www.w3.org/XML/1998/namespace\""),
								"");
				feedString = feedString
						.replaceAll(
								EscapeChars
										.forRegex("xmlns=\"http://www.w3.org/XML/1998/namespace\""),
								"");
				return feedString;
			}
			return null;
		} catch (PatternSyntaxException ex) {
			throw new TransformerException(ex);
		}
	}

}
