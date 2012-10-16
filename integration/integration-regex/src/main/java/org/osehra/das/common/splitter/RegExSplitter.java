package org.osehra.das.common.splitter;

import org.osehra.das.common.array.ArrayUtil;
import org.osehra.das.common.regex.RegExUtil;
import org.osehra.das.common.validation.NullChecker;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Required;

/**
 * Splits a string based on regular expression.
 * 
 * @author Julian Jewel
 */
public class RegExSplitter implements Splitter<String, List<String>> {

	/**
	 * Add the input string as an output.
	 * @uml.property  name="addInput"
	 */
	private boolean addInput = false;
	/**
	 * The regular expression pattern.
	 * @uml.property  name="expression"
	 */
	private Pattern expression;

	/**
	 * Set the flag to add the input message in the output.
	 * @param theAddInput  adds the input message in the output
	 * @uml.property  name="addInput"
	 */
	public final void setAddInput(final boolean theAddInput) {
		this.addInput = theAddInput;
	}

	/**
	 * Set the regular expression.
	 * 
	 * @param theExpression
	 *            the regular expression
	 */
	@Required
	public final void setExpression(final String theExpression) {
		this.expression = Pattern.compile(theExpression, Pattern.DOTALL
				| Pattern.MULTILINE);
	}

	/**
	 * Split the messages using the regular expression.
	 * 
	 * @param message
	 *            the input message
	 * @return the split strings
	 * @throws SplitterException
	 *             exception when splitting
	 */
	@Override
	public final List<String> split(final String message)
			throws SplitterException {

		if (NullChecker.isEmpty(message)) {
			return null;
		}

		List<String> documents = RegExUtil.getFirstMatchValues(message,
				this.expression);
		if (this.addInput) {
			documents = ArrayUtil.safeAdd(documents, message);
		}
		return documents;
	}
}
