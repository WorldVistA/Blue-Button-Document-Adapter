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
import org.osehra.das.wrapper.nwhin.WrapperResource;

public class NwhinDataRetrieverTest extends AbstractDateAwareTests implements C32DocumentDao, C32DocumentEntityFactory, WrapperResource {
	NwhinDataRetriever retriever;
	List<C32DocumentEntity> insertedList;
	List<C32DocumentEntity> updatedList;
	List<C32DocumentEntity> docList;
	String returnXml;
	String ptNameToAssert;
	String ptIdToAssert;
	boolean nwhinError = false;
	boolean parsingError = false;
	
	@Before
	public void setup() {
		retriever = new NwhinDataRetriever();
		retriever.setAsyncMessageFormat(new AsyncRetrieveMessageFormat());
		retriever.setC32DocumentDao(this);
		retriever.setDocumentFactory(this);
		retriever.setWrapperResource(this);
		
		insertedList = new ArrayList<C32DocumentEntity>();
		updatedList = new ArrayList<C32DocumentEntity>();
		ptIdToAssert = null;
		docList = null;
		returnXml = null;
		ptNameToAssert = null;
		nwhinError = false;
		parsingError = false;
	}
	
	@Test
	public void onMessage_happyPath() {
		ptIdToAssert = "112233v10";
		setupDocList(ptIdToAssert, ptIdToAssert);
		retriever.onMessage(new TestTextMessage("20120908070605:112233v10:fred"));
		Assert.assertEquals(0, insertedList.size());
		Assert.assertEquals(1, updatedList.size());
		Assert.assertEquals(returnXml, updatedList.get(0).getDocument());
		Assert.assertEquals(ptIdToAssert, updatedList.get(0).getDocumentPatientId());
		Assert.assertEquals(ptIdToAssert, updatedList.get(0).getIcn());
		assertDatePartEqualsToday(updatedList.get(0).getCreateDate());
	}

	@Test
	public void onMessage_errorWrongPtId() {
		ptIdToAssert = "112233v01";
		String docPtId = "112233v02";
		setupDocList(ptIdToAssert, docPtId);
		retriever.onMessage(new TestTextMessage("20120908070605:112233v01:fred"));
		Assert.assertEquals(0, insertedList.size());
		Assert.assertEquals(1, updatedList.size());
		Assert.assertEquals(BlueButtonConstants.ERROR_PTID_STATUS_STRING, updatedList.get(0).getDocument());
		Assert.assertEquals(docPtId, updatedList.get(0).getDocumentPatientId());
		Assert.assertEquals(ptIdToAssert, updatedList.get(0).getIcn());
		assertDatePartEqualsToday(updatedList.get(0).getCreateDate());
	}
	
	@Test
	public void onMessage_docEmpty() {
		ptIdToAssert = "998877v99";
		setupDocList(ptIdToAssert, ptIdToAssert, "");
		retriever.onMessage(new TestTextMessage("20120908070605:998877v99:fred"));
		Assert.assertEquals(0, insertedList.size());
		Assert.assertEquals(1, updatedList.size());
		Assert.assertEquals(BlueButtonConstants.UNAVAILABLE_STATUS_STRING, updatedList.get(0).getDocument());
		Assert.assertEquals(ptIdToAssert, updatedList.get(0).getDocumentPatientId());
		Assert.assertEquals(ptIdToAssert, updatedList.get(0).getIcn());
		assertDatePartEqualsToday(updatedList.get(0).getCreateDate());
	}
	
	@Test
	public void onMessage_docNull() {
		ptIdToAssert = "998877v99";
		setupDocList(ptIdToAssert, ptIdToAssert, null);
		retriever.onMessage(new TestTextMessage("20120908070605:998877v99:fred"));
		Assert.assertEquals(0, insertedList.size());
		Assert.assertEquals(1, updatedList.size());
		Assert.assertEquals(BlueButtonConstants.UNAVAILABLE_STATUS_STRING, updatedList.get(0).getDocument());
		Assert.assertEquals(ptIdToAssert, updatedList.get(0).getDocumentPatientId());
		Assert.assertEquals(ptIdToAssert, updatedList.get(0).getIcn());
		assertDatePartEqualsToday(updatedList.get(0).getCreateDate());
	}
	
	@Test
	public void onMessage_error_nwhin() {
		nwhinError = true;
		ptIdToAssert = "998877vboom1";
		setupDocList(ptIdToAssert, ptIdToAssert, "boom boom");
		retriever.onMessage(new TestTextMessage("20120908070605:" + ptIdToAssert + ":fred"));
		Assert.assertEquals(0, insertedList.size());
		Assert.assertEquals(1, updatedList.size());
		Assert.assertEquals(BlueButtonConstants.ERROR_C32_STATUS_STRING, updatedList.get(0).getDocument());
		Assert.assertEquals(ptIdToAssert, updatedList.get(0).getDocumentPatientId());
		Assert.assertEquals(ptIdToAssert, updatedList.get(0).getIcn());
		assertDatePartEqualsToday(updatedList.get(0).getCreateDate());
	}
	
	@Test
	public void onMessage_error_parsing() {
		parsingError = true;
		ptIdToAssert = "998877vboom2";
		setupDocList(ptIdToAssert, ptIdToAssert, "boom boom #2");
		retriever.onMessage(new TestTextMessage("20120908070605:" + ptIdToAssert + ":fred"));
		Assert.assertEquals(0, insertedList.size());
		Assert.assertEquals(1, updatedList.size());
		Assert.assertEquals(BlueButtonConstants.ERROR_C32_PARSE_STATUS_STRING, updatedList.get(0).getDocument());
		Assert.assertEquals(ptIdToAssert, updatedList.get(0).getDocumentPatientId());
		Assert.assertEquals(ptIdToAssert, updatedList.get(0).getIcn());
		assertDatePartEqualsToday(updatedList.get(0).getCreateDate());
	}
	
	protected void setupDocList(String ptId, String docPtId) {
		setupDocList(ptId, docPtId, "<stuff></stuff>");
	}
	
	protected void setupDocList(String ptId, String docPtId, String docText) {
		ptNameToAssert = "fred";
		returnXml = docText;
		docList = new ArrayList<C32DocumentEntity>();
		docList.add(new C32DocumentEntity(ptId, docPtId, new java.sql.Timestamp(new Date().getTime()), BlueButtonConstants.INCOMPLETE_STATUS_STRING));
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
		if (parsingError) {
			throw new RuntimeException("just stopping to say 'hi'");
		}
		return new C32DocumentEntity(ptId, "112233v10", new java.sql.Timestamp(new Date().getTime()), xml);
	}
	
	//================

	@Override
	public Object getDomainXml(String patientId, String userName) {
		if (nwhinError) {
			throw new RuntimeException("once upon a time, there was a little bitty error...");
		}
		if (ptNameToAssert!=null) {
			Assert.assertEquals(ptNameToAssert, userName);
		}
		if (ptIdToAssert!=null) {
			Assert.assertEquals(ptIdToAssert, patientId);
		}
		return returnXml;
	}

}
