package org.osehra.das.common.date.hl7;

import org.osehra.das.common.validation.NullChecker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility for date and time.
 * 
 * @author Asha Amritraj
 */
public class HL7DateUtil {

	public static Date dateFromString(final String s) throws ParseException {

		if (NullChecker.isEmpty(s)) {
			return null;
		}
		Date date = null;
		SimpleDateFormat ofd = null;
		try {
			// 19920603131438-0500
			ofd = new SimpleDateFormat("yyyyMMddhhmmssZ");
			date = ofd.parse(s);
			return date;
		} catch (final ParseException ex) {
		}
		return date;
	}

	public static String yyyyMMddhhmmssZ(final Date date) throws ParseException {

		if (NullChecker.isEmpty(date)) {
			return null;
		}
		final SimpleDateFormat ofd = new SimpleDateFormat("yyyyMMddhhmmssZ");
		final String dateString = ofd.format(date);
		return dateString;
	}

}
