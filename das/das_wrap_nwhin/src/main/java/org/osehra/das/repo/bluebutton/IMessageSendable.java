package org.osehra.das.repo.bluebutton;

import java.util.Date;

public interface IMessageSendable {
	void sendRetrieveMessage(String patientId, String patientName, Date documentDate);
}
