package org.osehra.das.common.date;

import org.osehra.das.common.validation.Assert;
import org.osehra.das.common.validation.NullChecker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility for date and time.
 * 
 * @author Asha Amritraj
 */
public class DateUtil {

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
			String tempS = s;
			if (tempS.length() > 8) {
				tempS = tempS.substring(0, 8);
			}
			ofd = new SimpleDateFormat("yyyyMMdd");
			date = ofd.parse(tempS);
		}
		return date;
	}

	/**
	 * Default constructor.
	 */
	protected DateUtil() {
	}

}
