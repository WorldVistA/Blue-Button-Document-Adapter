package org.osehra.das.repo.bluebutton;

import java.util.Date;
import java.util.List;

import org.osehra.das.C32Document;

public interface Repository {
	List<DocStatus> getStatus(String patientId);
	C32Document getDocument(Date docDate, String patientId);
}
