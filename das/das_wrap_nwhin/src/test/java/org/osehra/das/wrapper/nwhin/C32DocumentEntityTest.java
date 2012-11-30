package org.osehra.das.wrapper.nwhin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class C32DocumentEntityTest {

	@Test
	public void compareTo() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(GregorianCalendar.YEAR, 2012);
		cal.set(GregorianCalendar.MONTH, 10);
		cal.set(GregorianCalendar.DATE, 20);
		cal.set(GregorianCalendar.HOUR_OF_DAY, 8);
		cal.set(GregorianCalendar.MINUTE, 20);
		cal.set(GregorianCalendar.SECOND, 0);
		Timestamp dt1 = new Timestamp(cal.getTime().getTime());
		C32DocumentEntity doc1 = new C32DocumentEntity("111", "1v", dt1, "blah blah blah");
		doc1.setId(1);

		cal.set(GregorianCalendar.DATE, 10);
		Timestamp dt2 = new Timestamp(cal.getTime().getTime());
		C32DocumentEntity doc2 = new C32DocumentEntity("222", "2v", dt2, "blu blu blu");
		doc2.setId(2);
		
		List<C32DocumentEntity> list = new ArrayList<C32DocumentEntity>(2);
		list.add(doc1);
		list.add(doc2);
		list.add(new C32DocumentEntity());
		
		Collections.sort(list);
		Assert.assertNull(list.get(0).getIcn());
		Assert.assertEquals("111", list.get(1).getIcn());
		Assert.assertEquals("222", list.get(2).getIcn());
		
		list.get(2).setIcn("111");
		Collections.sort(list);
		Assert.assertEquals("2v", list.get(1).getDocumentPatientId());
		Assert.assertEquals("1v", list.get(2).getDocumentPatientId());
		
		list.get(1).setCreateDate(list.get(2).getCreateDate());
		Collections.sort(list);
		Assert.assertEquals(1, list.get(1).getId());
		Assert.assertEquals(2, list.get(2).getId());
	}
	
}
