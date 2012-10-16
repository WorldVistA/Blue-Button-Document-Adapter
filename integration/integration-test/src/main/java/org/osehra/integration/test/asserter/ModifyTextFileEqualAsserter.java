package org.osehra.integration.test.asserter;

import org.osehra.integration.util.NullChecker;

public class ModifyTextFileEqualAsserter extends TextFileEqualAsserter {

	protected String replaceFirst;
	protected String replaceAll;
	protected String replacement;

	@Override
	public void assertTrue(final String value) {

		String modifiedValue = "";

		if (NullChecker.isNotEmpty(replaceFirst)) {
			modifiedValue = value.replaceFirst(replaceFirst, replacement);
		}
		else if (NullChecker.isNotEmpty(replaceAll)) {
			modifiedValue = value.replaceAll(replaceAll, replacement);
		}

		super.assertTrue(modifiedValue);

	}

	public void setReplaceFirst(String replaceFirst) {
		this.replaceFirst = replaceFirst;
	}

	public void setReplaceAll(String replaceAll) {
		this.replaceAll = replaceAll;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}


}
