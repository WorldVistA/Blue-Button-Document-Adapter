package org.osehra.das.common.transformer.hl7;

import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.validation.Assert;

import org.w3c.dom.Document;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.DefaultXMLParser;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.parser.XMLParser;

/**
 * The ER7 To XML Transformer.
 * 
 * @author Asha Amritraj
 */
public class ER7ToXML implements Transformer<String, Document> {

	@Override
	public Document transform(final String er7Message)
			throws TransformerException {
		Assert.assertNotEmpty(er7Message, "ER7 message cannot be null!");
		final PipeParser pp = new PipeParser();
		final XMLParser xp = new DefaultXMLParser();
		try {
			final Message mess = pp.parse(er7Message);
			final Document doc = xp.encodeDocument(mess);
			return doc;
		} catch (final EncodingNotSupportedException e) {
			throw new TransformerException(e);
		} catch (final HL7Exception e) {
			throw new TransformerException(e);
		}

	}

}
