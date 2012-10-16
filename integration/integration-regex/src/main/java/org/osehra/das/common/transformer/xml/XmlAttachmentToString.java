package org.osehra.das.common.transformer.xml;

import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.validation.NullChecker;
import org.osehra.das.common.xpath.XPathUtil;

import java.io.IOException;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlAttachmentToString implements Transformer<Document, byte[]> {

	/**
	 * @uml.property  name="contentExtractor"
	 * @uml.associationEnd  
	 */
	XPathExpression contentExtractor;
	/**
	 * @uml.property  name="isRepresentationBase64"
	 * @uml.associationEnd  
	 */
	XPathExpression isRepresentationBase64;

	@Required
	public void setContentExtractor(final String contentExtractor) {
		this.contentExtractor = XPathUtil.compileExpression(contentExtractor);
	}

	@Required
	public void setIsRepresentationBase64(final String isRepresentationBase64) {
		this.isRepresentationBase64 = XPathUtil
				.compileExpression(isRepresentationBase64);
	}

	@Override
	public byte[] transform(final Document doc) throws TransformerException {

		try {
			final Object node = this.contentExtractor.evaluate(doc,
					XPathConstants.NODE);
			if (NullChecker.isNotEmpty(node) && Node.class.isInstance(node)) {
				// Get the text
				String content = ((Node) node).getNodeValue().trim();
				if (NullChecker.isNotEmpty(content)) {
					content = content.replaceAll("\\s+", "");
					if (NullChecker.isNotEmpty(this.isRepresentationBase64)) {
						if ((Boolean) this.isRepresentationBase64.evaluate(doc,
								XPathConstants.BOOLEAN)) {
							@SuppressWarnings("restriction")
							final byte decoded[] = new sun.misc.BASE64Decoder()
									.decodeBuffer(content);
							return decoded;
						} else {
							return content.getBytes();
						}
					} else {
						return content.getBytes();
					}
				}
			}
		} catch (final IOException ex) {
			throw new TransformerException(ex);
		} catch (final XPathExpressionException ex) {
			throw new TransformerException(ex);
		}
		return null;
	}
}
