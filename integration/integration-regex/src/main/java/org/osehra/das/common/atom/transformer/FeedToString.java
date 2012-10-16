package org.osehra.das.common.atom.transformer;

import org.osehra.das.common.string.EscapeChars;
import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.validation.NullChecker;

import org.apache.abdera.model.Feed;

public class FeedToString implements Transformer<Feed, String> {

	/**
	 * @uml.property  name="stylesheetRef"
	 */
	String stylesheetRef;

	/**
	 * @param stylesheetRef
	 * @uml.property  name="stylesheetRef"
	 */
	public void setStylesheetRef(final String stylesheetRef) {
		this.stylesheetRef = stylesheetRef;
	}

	@Override
	public String transform(final Feed feed) throws TransformerException {
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
	}

}
