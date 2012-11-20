package org.osehra.das.repo.bluebutton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.osehra.das.C32Document;
import org.osehra.das.wrapper.nwhin.C32DocumentEntity;

public class RepositoryImpl extends AbstractC32DaoAware implements Repository {
	protected IMessageSendable messageSender;
	protected static final Log logger = LogFactory.getLog(RepositoryImpl.class);
	
	public IMessageSendable getMessageSender() {
		return messageSender;
	}

	public void setMessageSender(IMessageSendable messageSender) {
		this.messageSender = messageSender;
	}

	@Override
	public List<DocStatus> getStatus(String patientId, String patientName) {
		List<C32DocumentEntity> docList = getAllStoredDocuments(patientId);

		Date today = new Date();
		
		if (getDocumentByDate(today, docList)==null) {
			saveIncompleteDocument(today, patientId);
			sendMessageToRetrieve(today, patientId, patientName);
			return getDocStatusesKnown(patientId, today, docList);
		}
		return buildStatusList(docList);
	}

	@Override
	public C32Document getDocument(Date docDate, String patientId) {
		List<C32DocumentEntity> docList = getAllStoredDocuments(patientId); 
		C32DocumentEntity doc = getDocumentByDate(docDate, docList);
		if (doc==null) {
			throw new RuntimeException("document for date:" + docDate + " & id:" + patientId + " does not exist");
		}
		return getC32Doc(doc);
	}

	protected void saveIncompleteDocument(Date docDate, String ptId) {
		getC32DocumentDao().insert(new C32DocumentEntity(ptId, ptId, new java.sql.Date(docDate.getTime()), BlueButtonConstants.INCOMPLETE_STATUS_STRING));
	}
	
	protected C32Document getC32Doc(C32DocumentEntity doc) {
		C32Document newDoc = new C32Document();
		String xmlDoc = doc.getDocument();
		if (xmlDoc==null) {
			xmlDoc = "";
		}
		newDoc.setDocument(Base64.encodeBase64String(xmlDoc.getBytes()));
		newDoc.setPatientId(doc.getIcn());
		return newDoc;
	}

	protected C32DocumentEntity getDocumentByDate(Date today, List<C32DocumentEntity> docList) {
		if (docList!=null && !docList.isEmpty()) {
			for (int i=0;i<docList.size();i++) {

				LocalDate fileDate = LocalDate.fromDateFields(docList.get(i).getCreateDate());
				LocalDate todayDate = LocalDate.fromDateFields(today);

				if (todayDate.equals(fileDate)) {
					return docList.get(i);
				}
			}
		}
		return null;
	}

	protected List<C32DocumentEntity> getAllStoredDocuments(String patientId) {
		return getC32DocumentDao().getAllDocuments(patientId);
	}

	protected List<DocStatus> buildStatusList(List<C32DocumentEntity> docList) {
		if (docList==null || docList.isEmpty()) {
			return null;
		}
		List<DocStatus> statusList = new ArrayList<DocStatus>(docList.size());
		for (int i=0;i<docList.size();i++) {
			statusList.add(new DocStatus(docList.get(i).getIcn(), docList.get(i).getCreateDate(), BlueButtonConstants.COMPLETE_STATUS_STRING));
		}
		return statusList;
	}

	protected List<DocStatus> getDocStatusesKnown(String ptId, Date today, List<C32DocumentEntity> docList) {
		if (docList==null || docList.isEmpty()) {
			List<DocStatus> statusList = new ArrayList<DocStatus>(1);
			statusList.add(getIncompleteDocStatus(today, ptId));
			return statusList;
		}
		
		int arraySize = docList.size() + 1;
		List<DocStatus> statusList = new ArrayList<DocStatus>(arraySize);

		for (int i=0;i<docList.size();i++) {
			statusList.add(new DocStatus(docList.get(i).getIcn(), docList.get(i).getCreateDate(), BlueButtonConstants.COMPLETE_STATUS_STRING));
		}
		
		statusList.add(getIncompleteDocStatus(today, ptId));
		return statusList;		

	}
	
	protected DocStatus getIncompleteDocStatus(Date aDate, String anId) {
		return new DocStatus(anId, aDate, BlueButtonConstants.INCOMPLETE_STATUS_STRING);
	}

	protected void sendMessageToRetrieve(final Date today, final String patientId, final String ptName) {
		getMessageSender().sendRetrieveMessage(patientId, ptName, today);
	}

}