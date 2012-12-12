package org.osehra.das.repo.bluebutton;

import java.util.Date;

/**
 * Interface to send a data retrieval message.  Meant for an implementation that will
 * handle the message asynchronously.
 * @author Dept of VA
 *
 */
public interface MessageSendable {
	
	/**
	 * 
	 * @param patientId Patient ID
	 * @param patientName Patient name
	 * @param documentDate Document date
	 */
	void sendRetrieveMessage(String patientId, String patientName, Date documentDate);
}
