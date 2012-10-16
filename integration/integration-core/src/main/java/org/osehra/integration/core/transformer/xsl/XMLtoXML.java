package org.osehra.integration.core.transformer.xsl;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;

/**
 * Simple XML to XML transformer using a resource.
 */
public class XMLtoXML extends DefaultXSLTransformer implements
		Transformer<Document, Document> {

	@Override
	public Document transform(final Document sourceMessage)
			throws TransformerException {

		try {
			final DOMSource source = new DOMSource(sourceMessage);
			Document result;
			DOMResult target = new DOMResult();
			target = (DOMResult) super.transform(source, target);
			result = (Document) target.getNode();
			if (NullChecker.isEmpty(result)) {
				throw new TransformerException("Empty output from transformer!");
			}
			return result;

		} catch (final javax.xml.transform.TransformerException ex) {
			throw new TransformerException(ex);
		}
	}
}
