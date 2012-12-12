package org.osehra.das.repo.bluebutton;

import java.util.Date;
import java.util.List;

import org.osehra.das.C32Document;

/**
 * C32 repository interface.
 * @author Dept of VA
 *
 */
public interface Repository {
	
	/**
	 * Returns a list of DocStatus's.  Implementations are expected to query a C32DocumentDao
	 * for the last 2 documents, and if the latest document does not have todays date, then send
	 * a C32 retrieval message request to request one.
	 *  
	 * @param patientId Patient ID
	 * @param patientName Patient name
	 * @return List a DocStatus's
	 */
	List<DocStatus> getStatus(String patientId, String patientName);
	
	/**
	 * Returns a C32Document from the document date and patient ID identified by a DocStatus with a 
	 * status set to "COMPLETE".  Implementations are expected to transform the XML document in the 
	 * C32Document to Base64 encoding.
	 * 
	 * @param docDate Document date
	 * @param patientId Patient ID
	 * @return C32 Document
	 */
	C32Document getDocument(Date docDate, String patientId);
}
