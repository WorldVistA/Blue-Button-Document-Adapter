package org.osehra.das.repo.bluebutton;

import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;

public abstract class AbstractDateAwareTests {

	protected void assertDateEquals(Date aDate, int year, int month, int day, int hour, int minute, int second) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(aDate);
		Assert.assertEquals(year, cal.get(GregorianCalendar.YEAR));
		Assert.assertEquals(month, cal.get(GregorianCalendar.MONTH));
		Assert.assertEquals(day, cal.get(GregorianCalendar.DATE));
		Assert.assertEquals(hour, cal.get(GregorianCalendar.HOUR_OF_DAY));
		Assert.assertEquals(minute, cal.get(GregorianCalendar.MINUTE));
		Assert.assertEquals(second, cal.get(GregorianCalendar.SECOND));
	}

	protected java.sql.Date getDate(int year, int month, int day, int hour, int minute, int second) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(GregorianCalendar.YEAR, year);
		cal.set(GregorianCalendar.MONTH, month);
		cal.set(GregorianCalendar.DATE, day);
		cal.set(GregorianCalendar.HOUR_OF_DAY, hour);
		cal.set(GregorianCalendar.MINUTE, minute);
		cal.set(GregorianCalendar.SECOND, second);
		return new java.sql.Date(cal.getTime().getTime());
	}

}
