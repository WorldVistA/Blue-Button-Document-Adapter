package org.osehra.integration.core.validator.xml;

import org.osehra.integration.core.validator.ValidatorException;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;
import org.osehra.integration.util.XPathUtil;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * The XPath validator.
 * 
 * @author Julian Jewel
 */
public class XPathValidator implements
		org.osehra.integration.core.validator.Validator<Document> {

	XPathExpression xPathExpression;

	@Required
	public void setxPathExpression(final String xPathExpression) {
		this.xPathExpression = XPathUtil.compileExpression(xPathExpression);
	}

	@Override
	public final boolean validate(final Document message)
			throws ValidatorException {

		Assert.assertNotEmpty(message, "Message cannot be null!");

		try {
			final java.lang.Boolean booleanValue = (java.lang.Boolean) this.xPathExpression
					.evaluate(message, XPathConstants.BOOLEAN);
			if (NullChecker.isNotEmpty(booleanValue) && booleanValue) {
				return true;
			}
			return false;
		} catch (final XPathException e) {
			throw new ValidatorException(e);
		}
	}

}
