package org.osehra.das.repo.bluebutton;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osehra.das.IFormatTS;

public class AsyncRetrieveMessageFormatTest extends AbstractDateAwareTests {
	IFormatTS msgFormat;

	@Before
	public void setup() {
		msgFormat = new AsyncRetrieveMessageFormat();
	}
	
	@Test
	public void parse() {
		Object item = msgFormat.parse("20121110090807:1234:fred");
		Assert.assertNotNull(item);
		Assert.assertTrue(item instanceof AsyncRetrieveMessage);
		AsyncRetrieveMessage msg = (AsyncRetrieveMessage)item;
		Assert.assertEquals("1234", msg.getPatientId());
		Assert.assertEquals("fred", msg.getPatientName());
		assertDateEquals(msg.getDate(), 2012, 10, 10, 9, 8, 7);
	}

	@Test
	public void formatText() {
		String text = msgFormat.formatObject(new AsyncRetrieveMessage(getDate(2012, 10, 10, 16, 15, 14), "223344", "jill"));
		Assert.assertEquals("20121110161514:223344:jill", text);
	}
	
}
