package org.osehra.integration.test.asserter;

import org.osehra.integration.test.util.resource.ResourceUtil;
import org.osehra.integration.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

public class InputStreamAsserter implements Asserter<InputStream> {

	@Autowired
	protected ApplicationContext applicationContext;

	protected String validText;

	@Override
	public void assertTrue(final InputStream stream) {
		String tempOutText = "";
		try {
			tempOutText = convertStreamtoString(stream).trim();
		} catch (IOException e) {
			Assert.fail("Cannot convert stream to string for comparison");
		}
		String tempValidText = this.validText.trim();
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

	private String convertStreamtoString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
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
