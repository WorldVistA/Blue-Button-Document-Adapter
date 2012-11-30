package org.osehra.das.repo.bluebutton;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osehra.das.wrapper.nwhin.C32DocumentEntity;
import org.osehra.das.wrapper.nwhin.C32DocumentEntityFactory;
import org.osehra.das.wrapper.nwhin.IWrapperResource;

public class NwhinDataRetrieverTest extends AbstractDateAwareTests implements IC32DocumentDao, C32DocumentEntityFactory, IWrapperResource {
	NwhinDataRetriever retriever;
	List<C32DocumentEntity> insertedList;
	List<C32DocumentEntity> updatedList;
	List<C32DocumentEntity> docList;
	String returnXml;
	String ptIdToAssert;
	String ptNameToAssert;
	
	@Before
	public void setup() {
		retriever = new NwhinDataRetriever();
		retriever.setAsyncMessageFormat(new AsyncRetrieveMessageFormat());
		retriever.setC32DocumentDao(this);
		retriever.setDocumentFactory(this);
		retriever.setWrapperResource(this);
		
		insertedList = new ArrayList<C32DocumentEntity>();
		updatedList = new ArrayList<C32DocumentEntity>();
		returnXml = null;
		ptIdToAssert = null;
		ptNameToAssert = null;
	}
	
	@Test
	public void onMessage() {
		ptNameToAssert = "fred";
		ptIdToAssert = "112233v10";
		returnXml = "<stuff></stuff>";
		docList = new ArrayList<C32DocumentEntity>();
		docList.add(new C32DocumentEntity(ptIdToAssert, ptIdToAssert, new java.sql.Timestamp(new Date().getTime()), BlueButtonConstants.INCOMPLETE_STATUS_STRING));
		retriever.onMessage(new TestTextMessage("20120908070605:112233v10:fred"));
		Assert.assertEquals(0, insertedList.size());
		Assert.assertEquals(1, updatedList.size());
		Assert.assertEquals(returnXml, updatedList.get(0).getDocument());
		Assert.assertEquals("112233v10", updatedList.get(0).getDocumentPatientId());
		Assert.assertEquals(ptIdToAssert, updatedList.get(0).getIcn());
		assertDatePartEqualsToday(updatedList.get(0).getCreateDate());
	}
	
	protected void assertDatePartEqualsToday(Date aDate) {
		GregorianCalendar calExpected = new GregorianCalendar();
		GregorianCalendar calTestDate = new GregorianCalendar();
		calTestDate.setTime(aDate);
		Assert.assertEquals(calExpected.get(GregorianCalendar.YEAR), calTestDate.get(GregorianCalendar.YEAR));
		Assert.assertEquals(calExpected.get(GregorianCalendar.MONTH), calTestDate.get(GregorianCalendar.MONTH));
		Assert.assertEquals(calExpected.get(GregorianCalendar.DATE), calTestDate.get(GregorianCalendar.DATE));
	}

	//================

	@Override
	public void insert(C32DocumentEntity document) {
		insertedList.add(document);
	}
	
	@Override
	public void update(C32DocumentEntity document) {
		updatedList.add(document);
	}

	@Override
	public List<C32DocumentEntity> getAllDocuments(String patientId) {
		return docList;
	}

	//================

	public C32DocumentEntity createDocument(String ptId, String xml) {
		return new C32DocumentEntity(ptId, "112233v10", new java.sql.Timestamp(new Date().getTime()), xml);
	}
	
	//================

	@Override
	public Object getDomainXml(String patientId, String userName) {
		if (ptNameToAssert!=null) {
			Assert.assertEquals(ptNameToAssert, userName);
		}
		if (ptIdToAssert!=null) {
			Assert.assertEquals(ptIdToAssert, patientId);
		}
		return returnXml;
	}

}
