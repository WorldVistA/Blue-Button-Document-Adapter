package org.osehra.das;

import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleDateFormatTSTest {
	FormatTS simpleDateFormat;
	
	@Before
	public void setup() {
		simpleDateFormat = new SimpleDateFormatTS();
	}
	
	@Test
	public void formatObject() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(2012, 10, 19, 6, 2, 1);
		String date1 = simpleDateFormat.formatObject(cal.getTime());
		Assert.assertEquals("20121119060201", date1);
	}
	
	@Test
	public void parse() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(2012, 9, 9, 8, 7, 6);
		cal.set(GregorianCalendar.MILLISECOND, 0);
		Assert.assertEquals(cal.getTime(), simpleDateFormat.parse("20121009080706"));
	}
}
