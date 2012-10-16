package org.osehra.das.common.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtil {

	public final static Pattern REPLACE_BLANKS_PATTERN = Pattern.compile(
			"^[ \\t]*$\\r?\\n", Pattern.DOTALL | Pattern.MULTILINE);

	public final static Pattern REPLACE_SPACES_PATTERN = Pattern.compile(
			"^[ \t]+|[ \t]+$", Pattern.DOTALL | Pattern.MULTILINE);

	public static String getFirstMatchValue(final String text,
			final Pattern pattern) {

		if (pattern == null) {
			return "";
		}

		if ((null == text) || ((text != null) && (text.length() <= 0))) {
			return "";
		}
		final Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			final String groupText = matcher.group(1);
			if ((groupText != null) && (groupText.length() > 0)) {
				return groupText.trim();
			}
		}
		return "";
	}

	public static List<String> getFirstMatchValues(final String text,
			final Pattern pattern) {
		final List<String> arrList = new ArrayList<String>();

		if (pattern == null) {
			return arrList;
		}

		if ((null == text) || ((text != null) && (text.length() <= 0))) {
			return arrList;
		}

		final Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			final String groupText = matcher.group(1);
			if ((groupText != null) && (groupText.length() > 0)) {
				arrList.add(groupText.trim());
			}
		}
		return arrList;
	}

	protected RegExUtil() {
	}
}
