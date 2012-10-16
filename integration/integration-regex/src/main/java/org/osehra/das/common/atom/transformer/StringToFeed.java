package org.osehra.das.common.atom.transformer;

import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;

import java.io.StringReader;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Feed;

public class StringToFeed implements Transformer<String, Feed> {

	@Override
	public Feed transform(final String feed) throws TransformerException {
		final StringReader reader = new StringReader(feed);
		final Document<Feed> feedDoc = Abdera.getNewParserFactory().getParser()
				.parse(reader);
		return feedDoc.getRoot();
	}
}
