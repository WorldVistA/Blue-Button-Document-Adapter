package org.osehra.integration.core.transformer.xml;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;
import org.osehra.integration.util.XPathUtil;

import java.io.IOException;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlAttachmentToString implements Transformer<Document, byte[]> {

	/**
	 * @uml.property name="contentExtractor"
	 * @uml.associationEnd
	 */
	XPathExpression contentExtractorExpression;
	/**
	 * @uml.property name="isRepresentationBase64"
	 * @uml.associationEnd
	 */
	XPathExpression isRepresentationBase64Expression;

	@Required
	public void setContentExtractorExpression(
			final String contentExtractorExpression) {
		this.contentExtractorExpression = XPathUtil
				.compileExpression(contentExtractorExpression);
	}

	@Required
	public void setIsRepresentationBase64Expression(
			final String isRepresentationBase64Expression) {
		this.isRepresentationBase64Expression = XPathUtil
				.compileExpression(isRepresentationBase64Expression);
	}

	@Override
	public byte[] transform(final Document doc) throws TransformerException {

		try {
			final Object node = this.contentExtractorExpression.evaluate(doc,
					XPathConstants.NODE);
			if (NullChecker.isNotEmpty(node) && Node.class.isInstance(node)) {
				// Get the text
				String content = ((Node) node).getNodeValue().trim();
				if (NullChecker.isNotEmpty(content)) {
					content = content.replaceAll("\\s+", "");
					if (NullChecker
							.isNotEmpty(this.isRepresentationBase64Expression)) {
						if ((Boolean) this.isRepresentationBase64Expression
								.evaluate(doc, XPathConstants.BOOLEAN)) {
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
