package org.osehra.integration.feed.transformer;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.EscapeChars;
import org.osehra.integration.util.NullChecker;

import java.io.StringReader;
import java.util.regex.PatternSyntaxException;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.ParseException;

public class StringToFeed implements Transformer<String, Feed> {

	@Override
	public Feed transform(final String feed) throws TransformerException {
		try {
			if (NullChecker.isNotEmpty(feed)) {
				// TODO: Cleanup - Fix this using DOM or research why XMLNS is being
				// added to entry baseURI
				String feedString = feed;
				feedString = feedString
				.replaceAll(
						EscapeChars
								.forRegex("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"),
						"");
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
				final StringReader reader = new StringReader(feedString);
				final Document<Feed> feedDoc = Abdera.getNewParserFactory().getParser()
						.parse(reader);
				return feedDoc.getRoot();
			}
			return null;
		} catch (PatternSyntaxException ex) {
			throw new TransformerException(ex);
		} catch (ParseException ex) {
			throw new TransformerException(ex);
		}
	}
}
