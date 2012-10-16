package org.osehra.integration.util;

import org.osehra.integration.core.transformer.TransformerException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * Utility for date and time.
 *
 * @author Asha Amritraj
 */
public class DateUtil {

	/**
	 * This method is to convert ISO 8601:1988 format into ISO 8824-1987 format.
	 * If the input datetime is not old ISO 8601:1988 format (4th & 7th char !=
	 * '-'), then the input string will be returned.
	 *
	 * @param oldISO
	 *            ISO 8601:1988 time format as: YYYY-MM-DDTHH:MM:SS.dddTZD, with
	 *            a timezone TZD on the end. The timezone is +/-HH:MM from GMT
	 *            or just Z for GMT.
	 * @return String ISO 8824-1987 time format as: YYYYMMDDHHmmSS.SSS+/-ZZZZ,
	 *         Timezone(+/-ZZZZ) in HHMM format.
	 */
	public static String convertISO8601To8824(final String oldISO) {
		// this regex pattern describes:
		// yyyy[-MM[-dd[THH:mm[:ss[.S[S[S]]]]]]][+/-ZH:ZM]
		final Pattern iso86011988 = Pattern
				.compile("((\\d{4})-(\\d{2})-(\\d{2})"
						+ "(?:T(\\d{2}):(\\d{2})(?::(\\d{2})(\\.\\d{1,3})??)??)??)"
						+ "((?:((?:\\+|-)\\d{2}):(\\d{2}))|Z)??");
		final Matcher match = iso86011988.matcher(oldISO);
		if (!match.matches()) {
			throw new IllegalArgumentException(
					"Invalid ISO 8601-1988 time format: " + oldISO);
		}

		final String dateTime = match.group(1);
		final String tzone = match.group(9);
		String formatString = "yyyy-MM-dd";
		if (dateTime.contains("T")) {
			formatString = "yyyy-MM-dd'T'HH:mm:ss.SSS";
		}
		final DateFormat dfIn = new SimpleDateFormat(formatString);
		if (null != tzone) {
			dfIn.setTimeZone(TimeZone.getTimeZone(tzone));
		}
		final Date dt = dfIn.parse(dateTime, new ParsePosition(0));

		DateFormat dfOut;
		if (null != tzone) {
			dfOut = new SimpleDateFormat("yyyyMMddHHmmss.SSSZ");
		} else if (formatString.contains("T")){
			dfOut = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
		} else {
			dfOut = new SimpleDateFormat("yyyyMMdd");
		}
		return dfOut.format(dt);
	}

	/**
	 * This method converts ISO 8601:1988 format into a specified output format.
	 * If the input datetime is not old ISO 8601:1988 format (4th & 7th char !=
	 * '-'), then the input string will be returned.
	 *
	 * @param oldISO
	 *            ISO 8601:1988 time format as: YYYY-MM-DDTHH:MM:SS.dddTZD, with
	 *            a timezone TZD on the end. The timezone is +/-HH:MM from GMT
	 *            or just Z for GMT.
	 * @param format
	 *            A string specifying a SimpleDateFormat, e.g. "yyyy-MM-dd".
	 * @return String date converted to specified output format.
	 */
	public static String convertISO8601ToText(final String oldISO, final String format) {

		final String inputString = DateUtil.convertISO8601To8824(oldISO);
		final Date date = DateUtil.dateFromString(inputString);
		final SimpleDateFormat sdFormat = new SimpleDateFormat(format);
		String outputString;
		outputString = sdFormat.format(date);
		return outputString;
	}

	// Convert date from CDA format (yyyyMMddHHmmss.SSSZ or subset)
	// to U62 format (yyyy-MM-dd'T'HH:mm:ss.SSSZ or subset)
	public static String convertCDAtoU62(final String cdaDate) throws TransformerException {
		String src = cdaDate;
		if (NullChecker.isEmpty(src)) {
			return "";
		}

		DateFormat dfIn;
		DateFormat dfOut;

		final int len = src.length();

		// SimpleDateFormat.parse throws exception if input doesn't match format exactly.
		// Set up formats case by case to make sure we get it right.

		if (len <= 8) {
			// Date only
			if (len == 4) {
				dfIn = new SimpleDateFormat("yyyy");
				dfOut = new SimpleDateFormat("yyyy");
			} else if (len == 6) {
				dfIn = new SimpleDateFormat("yyyyMM");
				dfOut = new SimpleDateFormat("yyyy-MM");
			} else if (len == 8) {
				dfIn = new SimpleDateFormat("yyyyMMdd");
				dfOut = new SimpleDateFormat("yyyy-MM-dd");
			} else {
				throw new IllegalArgumentException("Unrecognized date format: " + src);
			}
		} else if (len == 12) {
			dfIn = new SimpleDateFormat("yyyyMMddHHmm");
			dfOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		} else if (len < 14) {
			throw new IllegalArgumentException("Unrecognized date format: " + src);
		} else if (len == 14) {
			dfIn = new SimpleDateFormat("yyyyMMddHHmmss");
			dfOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		} else if ((src.contains("+")) || (src.contains("-")) || (src.contains("Z"))) {
			// SimpleDateFormat timezone indicator Z doesn't support two digits after + or -.  Adjust.
			if ((src.indexOf("+") == len - 3) || (src.indexOf("-") == len - 3)) {
				src = src + "00";
			}
			if (src.contains(".")) {
				dfIn = new SimpleDateFormat("yyyyMMddHHmmss.SSSZ");
				dfOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			} else {
				dfIn = new SimpleDateFormat("yyyyMMddHHmmssZ");
				dfOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			}
		} else if (src.contains(".")) {
			dfIn = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
			dfOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		} else {
			throw new IllegalArgumentException("Unrecognized date format: " + src);
		}

		Date dt;

		try {
			dt = dfIn.parse(src);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unrecognized date format: " + src);
		}

		return dfOut.format(dt);
	}

	public static Date dateFromString(final String dateString) {
		try {
			Assert.assertNotEmpty(dateString, "Date string cannot be empty!");
			return DateUtil.parseDateFromString(dateString);
		} catch (final ParseException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String formatDateyyyyMMddHHmmssSSSZ(final Date date) {
		if (NullChecker.isEmpty(date)) {
			return null;
		}
		final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.SSSZ");
		return df.format(date);
	}

	public static String getCurrentDateTimeyyyyMMddHHmmss() {
		final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(Calendar.getInstance().getTime());
	}

	/**
	 * Returns a TimeStamp for the current time on the server. This can be
	 * useful for a client which resides in another timezone or which has
	 * questionable date/time settings (like a PC). A client can base a query on
	 * the servers time rather than the clients time.
	 * <p>
	 * The time format is: YYYYMMDDHHMMSS.SSS, with a timezone on the end. The
	 * timezone is + or - ZZZZ(HHMM) from GMT or just Z for GMT.
	 *
	 * @return String - The time.
	 * @since jdk1.2
	 */
	public static String getCurrentTime() {
		final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.SSSZ");
		return df.format(Calendar.getInstance().getTime());
	}

	public static Date getRecentDate(final Date... dates) {
		if (NullChecker.isEmpty(dates)) {
			return null;
		}
		Date recent = dates[0];
		for (final Date date : dates) {
			if (recent.before(date)) {
				recent = date;
			}
		}
		return recent;
	}

	/**
	 * Attempts to parse a date formatted string. TODO: Not a good way to parse
	 * a date by exceptions. Refactor code! NOTE: Parse formats should be in
	 * length order.
	 *
	 * @param s
	 *            a date formatted string, 1 of 6 formats
	 * @return Date - the date
	 * @throws ParseException
	 *             exception from parsing the string TODO: Refactor method!!
	 */
	public static Date parseDateFromString(final String s)
			throws ParseException {

		if (NullChecker.isEmpty(s)) {
			return null;
		}
		Date date = null;
		SimpleDateFormat ofd = null;
		try {
			ofd = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
			date = ofd.parse(s);
			return date;
		} catch (final ParseException ex) {
		}
		if (NullChecker.isEmpty(date)) {
			try {
				ofd = new SimpleDateFormat("MMM dd, yyyy");
				date = ofd.parse(s);
				return date;
			} catch (final ParseException ex) {
			}
		}
		if (NullChecker.isEmpty(date)) {
			try {
				ofd = new SimpleDateFormat("MMM dd,yyyy");
				date = ofd.parse(s);
				return date;
			} catch (final ParseException ex) {
			}
		}
		if (NullChecker.isEmpty(date)) {
			try {
				ofd = new SimpleDateFormat("MM/dd/yyyy");
				date = ofd.parse(s);
				return date;
			} catch (final ParseException ex) {
			}
		}
		if (NullChecker.isEmpty(date)) {
			try {
				ofd = new SimpleDateFormat("MM/dd/yy");
				date = ofd.parse(s);
				return date;
			} catch (final ParseException ex) {
			}
		}
		if (NullChecker.isEmpty(date)) {
			try {
				ofd = new SimpleDateFormat("MM/dd/yy");
				date = ofd.parse(s);
				return date;
			} catch (final ParseException ex) {
			}
		}
		if (NullChecker.isEmpty(date)) {
			String tempS = s;
			if (tempS.length() > 8) {
				tempS = tempS.substring(0, 8);
			}
			ofd = new SimpleDateFormat("yyyyMMdd");
			date = ofd.parse(tempS);
		}
		return date;
	}
	 /*
	  * This method gets the current date and time and puts it in a format
	  * that includes the time zone.  The output will be in this format:
	  * 2012-06-02T11:52:45-04:00
	  */
	 public static String currenttDateWithTimeZone() throws Exception
	 {
	  String formattedDateTimeZone = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format( new Date() );
	  return formattedDateTimeZone;
	 }

	/**
	 * Default constructor.
	 */
	protected DateUtil() {
	}

}
