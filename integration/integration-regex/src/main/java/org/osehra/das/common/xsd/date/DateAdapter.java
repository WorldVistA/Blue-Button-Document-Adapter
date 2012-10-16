package org.osehra.das.common.xsd.date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;

/**
 * Date adapter for getting JAXB to use Date instead of XMLGregorianCalendar.
 * 
 * @author Asha Amritraj
 */
public class DateAdapter {
	public static Date parseDateTime(final String s) {
		return DatatypeConverter.parseDate(s).getTime();
	}

	public static String printDateTime(final Date dt) {
		final Calendar cal = new GregorianCalendar();
		cal.setTime(dt);
		return DatatypeConverter.printDate(cal);
	}
}
