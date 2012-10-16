package org.osehra.das.common.transformer.xsl;

import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.validation.NullChecker;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;

/**
 * Simple XML to XML transformer using a resource.
 */
public class XMLtoXML extends DefaultXSLTransformer implements
		Transformer<Document, Document> {

	@Override
	public final Document transform(final Document sourceMessage)
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
