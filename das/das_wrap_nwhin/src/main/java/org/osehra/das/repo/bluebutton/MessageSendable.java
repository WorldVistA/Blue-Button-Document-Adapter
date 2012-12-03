package org.osehra.das.repo.bluebutton;

import java.util.Date;

public interface MessageSendable {
	void sendRetrieveMessage(String patientId, String patientName, Date documentDate);
}
