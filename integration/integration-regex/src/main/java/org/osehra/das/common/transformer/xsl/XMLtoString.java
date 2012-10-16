package org.osehra.das.common.transformer.xsl;

import org.osehra.das.common.transformer.TransformerException;

import java.io.StringWriter;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * Transform from XML to String. StringWriter is used for output.
 * 
 * @author Asha Amritraj
 */
public class XMLtoString extends DefaultXSLTransformer
		implements
		org.osehra.das.common.transformer.Transformer<Document, String> {

	@Override
	public final String transform(final Document sourceMessage)
			throws TransformerException {

		final StringWriter stringWriter = new StringWriter();
		final DOMSource source = new DOMSource(sourceMessage);

		try {
			super.transform(source, new StreamResult(stringWriter));
		} catch (final javax.xml.transform.TransformerException ex) {
			throw new TransformerException(ex);
		}
		return stringWriter.toString();
	}

}
