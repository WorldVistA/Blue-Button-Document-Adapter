package org.osehra.integration.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Convenience methods for the Java String
 * 
 * @author Keith Roberts
 */
public class StringUtil {

	/**
	 * Escape the special character with a '/' character.
	 * 
	 * @param s
	 *            - The character to escape.
	 * @return String - A String whose character is escaped.
	 */
	public static String appendEscapeSequence(final Character s) {
		return StringUtil.appendEscapeSequence(s.toString());
	}

	/**
	 * Escape the special characters found by prepending a '/' character.
	 * 
	 * @param s
	 *            - The string to escape the characters.
	 * @return String - The string with all its special characters escaped.
	 */
	public static String appendEscapeSequence(final String s) {

		if (NullChecker.isEmpty(s)) {
			return s;
		}

		final String escapeChars = "\\|[].^${}*";
		final StringBuffer out = new StringBuffer();
		int i;
		char c;

		for (i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (escapeChars.indexOf(c) > -1) {
				out.append('\\');
			}
			out.append(c);
		}

		return out.toString();
	}

	/**
	 * Returns an array of strings of the specified size.
	 * 
	 * @param size
	 *            - length of the string to be put in each item of the array.
	 * @return String[] - the array of strings
	 */
	public static String[] breakString(final String str, final int size) {
		final int len = str.length();
		String[] result;

		if (size > len) {
			result = new String[1];
			result[0] = str;
			return result;
		}
		int arLen = len / size;
		// Add one to accomodate remaining characters in remainder.
		if (len % size != 0) {
			arLen++;
		}
		result = new String[arLen];
		int beg = 0;
		int end = size;
		for (int i = 0; i < arLen; i++) {
			if (end < len) {
				result[i] = str.substring(beg, end);
			} else {
				result[i] = str.substring(beg);
			}
			beg = end;
			end += size;
		}
		return result;
	}

	/**
	 * Determines if the input String contains anyone of the Strings in the vals
	 * param. It returns the position of the first String in vals it finds.
	 * 
	 * @param str
	 *            The String to scan.
	 * @param vals
	 *            The Strings to scan for in str.
	 * @return int - the position of the first String in vals it finds. TODO:
	 *         Remove assignments in if()
	 */
	public static int contains(final String str, final String[] vals) {
		int idx = -1;
		for (final String element : vals) {
			if (-1 != (idx = str.indexOf(element))) {
				return idx;
			}
		}
		return idx;
	}

	/**
	 * Truncate the string at 80 characters if it is greater than size.
	 * 
	 * @param str
	 *            - The string.
	 * @param size
	 *            - The size.
	 * @return String - A truncated string of 80 characters.
	 */
	public static String cut(final String str, final int size) {
		if (NullChecker.isEmpty(str)) {
			return str;
		}
		if (str.length() > size) {
			return str.substring(0, 80);
		} else {
			return str;
		}
	}

	/**
	 * Returns a string with inserted commas in the input value.
	 * 
	 * @param aNumber
	 *            - The number in which commas are inserted.
	 * @return String - the modified String
	 */
	public static String formatNumberCommas(final long aNumber) {
		final StringBuffer buffer = new StringBuffer(String.valueOf(aNumber));
		for (int i = buffer.length() - 3; i > 0; i -= 3) {
			if ((i > 0) && (buffer.charAt(i - 1) != '-')) {
				buffer.insert(i, ',');
			}
		}
		return buffer.toString();
	}

	/**
	 * Gets a string between the two specified delimiters. Searching begins at
	 * the end of the string.
	 * 
	 * @param str
	 *            - The string.
	 * @param begDelim
	 *            - Beginning delimiter.
	 * @param endDelim
	 *            - The ending delimiter.
	 * @return String - the string located
	 */
	public static String getLastToken(final String str, final String begDelim,
			final String endDelim) {
		final int beg = str.lastIndexOf(begDelim);
		final int end = str.lastIndexOf(endDelim);
		if ((beg > 0) && (end > beg)) {
			return str.substring(beg + 1, end);
		}
		if ((beg < 0) && (end > 0)) {
			return str.substring(0, end);
		}
		if ((beg > 0) && (end < 0)) {
			return str.substring(beg + 1);
		}
		return str;
	}

	/**
	 * Gets the token in a delimited string in the specified position.
	 * 
	 * @param str
	 *            - The string.
	 * @param pos
	 *            - The position identifying the token.
	 * @param delim
	 *            - The delimiter used to tokenize the string.
	 * @return String - the located String
	 * @deprecated - use substring(String,index,delimiter) instead.
	 */
	@Deprecated
	public static String getToken(final String str, final int pos,
			final String delim) {
		String token = str;
		final StringTokenizer tokenizer = new StringTokenizer(str, delim);
		for (int i = 0; (i < pos) && tokenizer.hasMoreTokens(); i++) {
			token = tokenizer.nextToken();
		}
		return token;
	}

	/**
	 * Returns true if the string contains an ascii character.
	 * 
	 * @param str
	 *            - string to verify against
	 * @return boolean - true or false
	 */
	public static boolean hasAscii(final String str) {
		final String cmp = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()_+=[]{};':\",./<>?";
		for (int i = 0; i < str.length(); i++) {
			if (cmp.indexOf(str.charAt(i)) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return true if the string contains a digit.
	 * 
	 * @param str
	 *            - string to verify against
	 * @return boolean - true or false
	 */
	public static boolean hasDigit(final String str) {
		final String cmp = "0123456789-";
		for (int i = 0; i < str.length(); i++) {
			if (cmp.indexOf(str.charAt(i)) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a string where HTML special characters are escaped.
	 * 
	 * @param o
	 *            - string to mod
	 * @return String - modified String
	 */
	public static String htmlEscape(final Object o) {
		return StringUtil.htmlEscape(o.toString());
	}

	/**
	 * Returns a string where HTML special characters are escaped.
	 * 
	 * @param s
	 *            - String to mod
	 * @return String - modified String
	 */
	public static String htmlEscape(final String s) {
		final StringBuffer sb = new StringBuffer();

		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);

			switch (c) {
			case '&':
				sb.append("&amp;");
				break;
			case '\"':
				sb.append("&quot;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '\'':
				sb.append("&#39");
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private static boolean isWhiteSpaceChar(final char testChar,
			final char[] whiteSpaceChars) {
		for (final char whiteSpaceChar : whiteSpaceChars) {
			if (whiteSpaceChar == testChar) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns an array of string from the Iterator.
	 * 
	 * @param iter
	 *            - The Iterator.
	 * @return String[] - array of string from the Iterator
	 */
	public static String[] makeArray(final Iterator<String> iter) {
		final ArrayList<String> list = new ArrayList<String>();
		while (iter.hasNext()) {
			final String item = iter.next();
			list.add(item);
		}
		final String[] result = new String[list.size()];
		final Iterator<String> ci = list.iterator();
		for (int i = 0; (i < result.length) && ci.hasNext(); i++) {
			result[i] = ci.next();
		}
		return result;
	}

	/**
	 * Returns an array of string from the List.
	 * 
	 * @param list
	 *            - The List.
	 * @return String[] - array of String from the List
	 */
	public static String[] makeArray(final List<String> list) {
		final String[] anArray = new String[list.size()];
		for (int i = 0; i < anArray.length; i++) {
			final Object obj = list.get(i);
			if (obj instanceof String) {
				anArray[i] = list.get(i);
			} else {
				return null;
			}
		}
		return anArray;
	}

	/**
	 * Returns an array parsed from inStr by the specified delimiter.
	 * 
	 * @param inStr
	 *            - The string to be parsed.
	 * @param delimeter
	 *            - The delimeter to use for parsing.
	 * @return String[] - array from the parsed Strings
	 */
	public static String[] makeArray(final String inStr, final String delimeter) {
		final ArrayList<String> list = new ArrayList<String>();
		final StringTokenizer tokenizer = new StringTokenizer(inStr, delimeter);
		while (tokenizer.hasMoreTokens()) {
			final String token = tokenizer.nextToken();
			list.add(token);
		}
		final String[] result = new String[list.size()];
		final Iterator<String> ci = list.iterator();
		for (int i = 0; (i < result.length) && ci.hasNext(); i++) {
			result[i] = ci.next();
		}
		return result;
	}

	/**
	 * Makes a byte array from the string.
	 * 
	 * @param str
	 *            - the string to be converted
	 * @return byte[] - a byte array
	 */
	public static byte[] makeByteArray(final String str) {
		final byte[] byteBuffer = new byte[str.length()];
		for (int i = 0; i < byteBuffer.length; i++) {
			byteBuffer[i] = (byte) str.charAt(i);
		}
		return byteBuffer;

	}

	/**
	 * Converts a typical camel case name into a displayable description. It
	 * makes the first character upper case and inserts a space before each
	 * subsequent upper case character.
	 * 
	 * @param str
	 *            - string to be converted
	 * @return String - polycap string
	 */
	public static String makeDisplayFromName(final String str) {
		final StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			final char ch = str.charAt(i);
			// If first char is lower case, capitalize it.
			if ((i == 0) && Character.isLowerCase(ch)) {
				buffer.append(Character.toUpperCase(ch));
			}
			// If char is a capital, insert a space.
			else if (Character.isUpperCase(ch)) {
				buffer.append(" " + ch);
			}
			// If char lower, insert as is.
			else {
				buffer.append(ch);
			}
		}
		return buffer.toString();
	}

	/**
	 * Returns a formatted string for the Enumeration. ie; [item1,item2,itemN]
	 * 
	 * @param anEnumerator
	 *            - String Enumerator
	 * @return String - comma separated string
	 */
	public static String makeString(final Enumeration<String> anEnumerator) {
		final StringBuffer buffer = new StringBuffer("[");
		if (anEnumerator == null) {
			buffer.append("null");
		} else {
			while (anEnumerator.hasMoreElements()) {
				buffer.append(anEnumerator.nextElement());
				if (anEnumerator.hasMoreElements()) {
					buffer.append(",");
				}
			}
		}
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * Returns a formatted string for the iterator. ie; [item1,item2,itemN]
	 * 
	 * @param iter
	 *            - string Iterator
	 * @return - comma seperated string
	 */
	public static String makeString(final Iterator<String> iter) {
		final StringBuffer buffer = new StringBuffer("[");
		if (iter == null) {
			buffer.append("null");
		} else {
			while (iter.hasNext()) {
				buffer.append(iter.next());
				if (iter.hasNext()) {
					buffer.append(",");
				}
			}
		}
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * Returns a formatted string for the List. ie; [item1,item2,itemN]
	 * 
	 * @param list
	 *            - the String List
	 * @return String - comma seperated String
	 */
	public static String makeString(final List<String> list) {
		return StringUtil.makeString(list, ",");
	}

	/**
	 * Returns a formatted string for the List with the specified delimiter. ie;
	 * [item1,item2,itemN]
	 * 
	 * @param list
	 *            - String List
	 * @param delimeter
	 *            - a specific delimeter
	 * @return String - the delimeter seperated String
	 */
	public static String makeString(final List<String> list,
			final String delimeter) {
		final StringBuffer buffer = new StringBuffer("[");
		if (list == null) {
			buffer.append("null");
		} else {
			for (int i = 0; i < list.size(); i++) {
				buffer.append(list.get(i));
				if (i != list.size() - 1) {
					buffer.append(delimeter);
				}
			}
		}
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * Returns a formatted string for the array. ie; [item1,item2,itemN]
	 * 
	 * @param array
	 *            - the Object array to convert
	 * @return String - the comma seperate String
	 */
	public static String makeString(final Object[] array) {
		return StringUtil.makeString(array, ",");
	}

	/**
	 * Returns a formatted string for the array with the specified delimiter.
	 * ie; [item1,item2,itemN]
	 * 
	 * @param array
	 *            - the Object array to convert
	 * @param delimeter
	 *            - a specific delimeter
	 * @return String - the delimeter seperated String
	 */
	public static String makeString(final Object[] array, final String delimeter) {
		final StringBuffer buffer = new StringBuffer("[");
		if (array == null) {
			buffer.append("null");
		} else {
			for (int i = 0; i < array.length; i++) {
				buffer.append(array[i]);
				if (i != array.length - 1) {
					buffer.append(delimeter);
				}
			}
		}
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * Returns a string describing the throwable object.
	 * 
	 * @param ex
	 *            - The throwable
	 * @return String - A formatted string.
	 */
	public static String makeString(final Throwable ex) {
		return StringUtil.makeString(ex, true);
	}

	/**
	 * Returns a string describing the throwable object, include the stacktrace
	 * if specified.
	 * 
	 * @param ex
	 *            - The throwable.
	 * @param doTrace
	 *            - Include the stack trace if true.
	 * @return String - A formatted string.
	 */
	public static String makeString(Throwable ex, final boolean doTrace) {
		final StringBuffer buffer = new StringBuffer();
		final StringWriter strWriter = new StringWriter();
		final PrintWriter writer = new PrintWriter(strWriter);
		while (ex != null) {
			buffer.append(ex.toString() + "\n");
			if (doTrace) {
				ex.printStackTrace(writer);
				buffer.append(strWriter.toString());
			}
			ex = ex.getCause();
		}
		return buffer.toString();
	}

	public static int mismatchAtIndex(final String str1, final String str2) {
		int len = 0;
		if (str1.length() < str2.length()) {
			len = str1.length();
		} else {
			len = str2.length();
		}
		for (int i = 0; i < len; i++) {
			if (str1.charAt(i) != str2.charAt(i)) {
				return i;
			}
		}
		return ((str1.length() > str2.length()) ? str2.length() : str1.length());
	}

	/**
	 * Remove the character at the specified position.
	 * 
	 * @param s
	 *            - The string.
	 * @param pos
	 *            - The position.
	 * @return String - The string without the character.
	 */
	public static String removeCharAt(final String s, final int pos) {
		if (s != null) {
			return s.substring(0, pos) + s.substring(pos + 1);
		}
		return s;
	}

	/**
	 * Remove all occurances of the character from the string.
	 * 
	 * @param inStr
	 *            - Remove characters from this string.
	 * @param chr
	 *            - The character to remove.
	 * @return String - a string with chr removed TODO: Remove assignments in
	 *         while()
	 */
	public static String removeChars(final String inStr, final char chr) {
		int idx = 0;
		final StringBuffer buffer = new StringBuffer(inStr);
		while ((idx = inStr.indexOf(chr, idx + 1)) >= 0) {
			buffer.deleteCharAt(idx);
		}
		return buffer.toString();
	}

	/**
	 * Remove the specified occurance strings from the input string.
	 * 
	 * @param inStr
	 *            - The string in which the characters are removed.
	 * @param removeChars
	 *            - The array of strings that are removed.
	 * @return String - a modified String
	 */
	public static String removeOccurances(final String inStr,
			final String[] removeChars) {
		final StringBuffer buffer = new StringBuffer(inStr);
		for (final String element : removeChars) {
			int begIdx = 0;
			while (begIdx >= 0) {
				begIdx = buffer.indexOf(element, begIdx);
				if (begIdx >= 0) {
					final int endIdx = begIdx + element.length();
					buffer.delete(begIdx, endIdx);
					begIdx++;
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * Removes all the white space from a string
	 * 
	 **/
	public static String removeWhiteSpace(final String inString,
			final char[] whiteSpaceChars) {
		final StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < inString.length(); i++) {
			if (!StringUtil.isWhiteSpaceChar(inString.charAt(i),
					whiteSpaceChars)) {
				buffer.append(inString.charAt(i));
			}
		}
		return buffer.toString();
	}

	/**
	 * Replace all occurances of the string with the replace string. Escape the
	 * special characters in the string and replace string.
	 * 
	 * @param template
	 *            - The string to search.
	 * @param theString
	 *            - The string to find.
	 * @param replaceString
	 *            - The string to replace.
	 * @return String - The string containing the replaced strings.
	 */
	public static String replaceAll(final String template,
			final String theString, final String replaceString) {
		return template.replaceAll(StringUtil.appendEscapeSequence(theString),
				StringUtil.appendEscapeSequence(replaceString));
	}

	/**
	 * Returns the substring of the indexed token as tokenized by the delimiter.
	 * 
	 * @param theString
	 *            - The string to tokenize.
	 * @param index
	 *            - The index value that identifies the substring to return.
	 * @param delimiter
	 *            - The delimiter used for tokenizing.
	 * @return String - The specified substring.
	 */
	public static String subString(final String theString, final int index,
			final char delimiter) {
		if ((theString == null)
				|| ((theString != null) && (theString.length() <= 0))) {
			return theString;
		}
		if (theString.indexOf(delimiter) > 0) {
			String delimiterString = String.valueOf(delimiter);
			if (delimiter == '*') {
				delimiterString = "\\" + delimiter;
			}
			final String splitString[] = theString.split(delimiterString);
			if ((splitString != null) && (splitString.length > index)) {
				return splitString[index];
			}
		}
		return theString;
	}

	/**
	 * Returns escape characters for xml.
	 * 
	 * @param s
	 *            - a String to be appended too
	 * @return String - a converted string
	 */
	public static String xmlEscape(final String s) {
		final StringBuffer sb = new StringBuffer();

		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);

			switch (c) {
			case '&':
				sb.append("&amp;");
				break;
			case '\"':
				sb.append("&quot;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
