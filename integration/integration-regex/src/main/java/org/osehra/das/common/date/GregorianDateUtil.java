package org.osehra.das.common.date;

import org.osehra.das.common.validation.Assert;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Utility for date and time.
 * 
 * @author Asha Amritraj
 */
public class GregorianDateUtil {

	public static Date getDateFromGregorianCalendar(
			final XMLGregorianCalendar calendar) {
		Assert.assertNotEmpty(calendar, "Calendar cannot be empty!");
		return new Date(calendar.getMillisecond());
	}

	/**
	 * Get the gregorian calendar by date.
	 * 
	 * @param date
	 *            the date
	 * @return XMLGregorianCalendar - gregorian calendar
	 * @throws RuntimeException
	 */
	public static XMLGregorianCalendar getGregorianCalendarByDate(
			final Date date) {
		Assert.assertNotEmpty(date, "Date cannot be empty!");
		final GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (final DatatypeConfigurationException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Get Gregorian calendar by date string.
	 * 
	 * @param dateString
	 *            the date string
	 * @return XMLGregorianCalendar - the XMLGregorianCalendar
	 * @throws ParseException
	 *             an exception when converting the string to Gregorian calendar
	 */
	public static XMLGregorianCalendar getGregorianCalendarByDateString(
			final String dateString) throws ParseException {
		Assert.assertNotEmpty(dateString, "Date string cannot be empty!");
		final Date date = DateUtil.parseDateFromString(dateString);
		return GregorianDateUtil.getGregorianCalendarByDate(date);
	}

	/**
	 * Default constructor.
	 */
	protected GregorianDateUtil() {
	}

}
