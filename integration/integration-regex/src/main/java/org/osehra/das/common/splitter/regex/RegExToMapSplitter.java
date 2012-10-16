package org.osehra.das.common.splitter.regex;

import org.osehra.das.common.splitter.Splitter;
import org.osehra.das.common.splitter.SplitterException;
import org.osehra.das.common.validation.NullChecker;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Required;

/**
 * Split the string into key/value pair based on position.
 * 
 * @author Asha Amritraj
 */
public class RegExToMapSplitter implements
		Splitter<String, Map<String, String>> {

	/**
	 * @uml.property  name="expression"
	 */
	private Pattern expression;
	/**
	 * @uml.property  name="matchGroupKeyPosition"
	 */
	private int matchGroupKeyPosition;
	/**
	 * @uml.property  name="matchGroupValuePosition"
	 */
	private int matchGroupValuePosition;

	@Required
	public void setExpression(final String expression) {
		this.expression = Pattern.compile(expression, Pattern.DOTALL
				| Pattern.MULTILINE);
	}

	/**
	 * @param matchGroupKeyPosition
	 * @uml.property  name="matchGroupKeyPosition"
	 */
	@Required
	public void setMatchGroupKeyPosition(final int matchGroupKeyPosition) {
		this.matchGroupKeyPosition = matchGroupKeyPosition;
	}

	/**
	 * @param matchGroupValuePosition
	 * @uml.property  name="matchGroupValuePosition"
	 */
	@Required
	public void setMatchGroupValuePosition(final int matchGroupValuePosition) {
		this.matchGroupValuePosition = matchGroupValuePosition;
	}

	@Override
	public Map<String, String> split(final String src) throws SplitterException {
		if (NullChecker.isEmpty(src)) {
			return null;
		}
		final Map<String, String> propertyMap = new HashMap<String, String>();

		final Matcher matcher = this.expression.matcher(src);
		while (matcher.find()) {
			final String key = matcher.group(this.matchGroupKeyPosition);
			final String value = matcher.group(this.matchGroupValuePosition);
			if (NullChecker.isNotEmpty(key) && NullChecker.isNotEmpty(value)) {
				propertyMap.put(key, value);
			}
		}
		return propertyMap;
	}
}
