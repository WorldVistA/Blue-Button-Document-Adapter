package org.osehra.integration.test.asserter;

import org.osehra.integration.test.util.resource.ResourceUtil;
import org.osehra.integration.util.StringUtil;

import java.io.IOException;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

public class TextFileEqualAsserter implements Asserter<String> {

	@Autowired
	protected ApplicationContext applicationContext;

	protected String validText;
	protected boolean removeWhiteSpace = true;
	protected final char[] whiteSpaceChars = { ' ', '\t', '\n', '\r' };

	@Override
	public void assertTrue(final String value) {
		String tempOutText = value.trim();
		String tempValidText = this.validText.trim();
		if (this.removeWhiteSpace) {
			tempOutText = StringUtil.removeWhiteSpace(value.trim(),
					this.whiteSpaceChars).trim();
			tempValidText = StringUtil.removeWhiteSpace(this.validText.trim(),
					this.whiteSpaceChars).trim();
		}
		if (!tempOutText.equals(tempValidText)) {
			// Assert.assertTrue("Text length is not equal "+tempOutText.length()+"!="+tempValidText.length(),tempOutText.length()
			// == tempValidText.length());
			Assert.fail("Text mismatches at index "
					+ StringUtil.mismatchAtIndex(tempOutText, tempValidText)
					+ " of string "
					+ ((tempOutText.length() > tempValidText.length()) ? "tempOutText"
							: "tempValidText") + ": tempOutText\n'"
					+ tempOutText + "'\n!=tempValidText\n'" + tempValidText
					+ "'");
		}
	}

	public void setRemoveWhiteSpace(final boolean flag) {
		this.removeWhiteSpace = flag;
	}

	/**
	 * Sets the resource that will be loaded as the validation document.
	 *
	 * @param filename
	 *            - The file name of the validation document.
	 */
	@Required
	public void setResource(final String filename) {
		try {
			this.validText = ResourceUtil.getTextResource(
					this.applicationContext, filename);
		} catch (final IOException e) {
			throw new RuntimeException("Loading of text resource failed: " + e);
		}
	}

}
