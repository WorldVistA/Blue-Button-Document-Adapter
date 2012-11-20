package org.osehra.das.repo.bluebutton;

import java.util.List;

import org.osehra.das.wrapper.nwhin.C32DocumentEntity;

public interface IC32DocumentDao {
	void insert(C32DocumentEntity document);
	void update(C32DocumentEntity document);
	List<C32DocumentEntity> getAllDocuments(String patientId);
}
