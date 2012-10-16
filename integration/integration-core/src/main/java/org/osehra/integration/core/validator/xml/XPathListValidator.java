package org.osehra.integration.core.validator.xml;

import org.osehra.integration.core.validator.ValidatorException;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;
import org.osehra.integration.util.XPathUtil;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * The XPath List validator.
 * 
 * @author Julian Jewel
 */
public class XPathListValidator implements
		org.osehra.integration.core.validator.Validator<Document> {

	List<XPathExpression> xPathExpressions;

	@Required
	public void setxPathExpression(final List<String> xPathExpressionList) {
		this.xPathExpressions = new ArrayList<XPathExpression>();
		for (final String expression : xPathExpressionList) {
			this.xPathExpressions.add(XPathUtil.compileExpression(expression));
		}
	}

	@Override
	public final boolean validate(final Document message)
			throws ValidatorException {

		Assert.assertNotEmpty(message, "Message cannot be null!");

		try {
			for (final XPathExpression expression : this.xPathExpressions) {
				final java.lang.Boolean booleanValue = (java.lang.Boolean) expression
						.evaluate(message, XPathConstants.BOOLEAN);
				if (NullChecker.isEmpty(booleanValue) || !booleanValue) {
					return false;
				}
			}
			return true;
		} catch (final XPathException e) {
			throw new ValidatorException(e);
		}
	}

}
