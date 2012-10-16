package org.osehra.integration.core.transformer.string;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Required;

public class PatternTransformer implements Transformer<String, Object> {

	/**
	 * @uml.property name="groupPosition"
	 */
	private Integer groupPosition = 1;
	/**
	 * @uml.property name="pattern"
	 */
	private Pattern pattern;
	/**
	 * @uml.property name="trimResult"
	 */
	private boolean trimResult = true;

	/**
	 * @param groupPosition
	 * @uml.property name="groupPosition"
	 */
	public void setGroupPosition(final Integer groupPosition) {
		this.groupPosition = groupPosition;
	}

	@Required
	public void setPattern(final String pattern) {
		final Pattern expressionPattern = Pattern.compile(pattern,
				Pattern.DOTALL | Pattern.MULTILINE);
		this.pattern = expressionPattern;
	}

	/**
	 * @param trimResult
	 * @uml.property name="trimResult"
	 */
	public void setTrimResult(final boolean trimResult) {
		this.trimResult = trimResult;
	}

	@Override
	public Object transform(final String source) throws TransformerException {
		final Matcher matcher = this.pattern.matcher(source);
		if (matcher.find()) {
			if (matcher.groupCount() >= this.groupPosition) {
				String value = matcher.group(this.groupPosition);
				if (NullChecker.isNotEmpty(value) && this.trimResult) {
					value = value.trim();
				}
				return value;
			}
		}
		return null;
	}
}
