package org.osehra.das.repo.bluebutton;

import java.util.List;

import org.osehra.das.wrapper.nwhin.C32DocumentEntity;

/**
 * Data access interface for 
 * <code>org.osehra.das.wrapper.nwhin.C32DocumentEntity</code>'s
 * 
 * @author Dept of VA
 *
 */
public interface C32DocumentDao {
	
	/**
	 * @param document A C32DocumentEntity
	 */
	void insert(C32DocumentEntity document);
	
	/**
	 * @param document A C32DocumentEntity
	 */
	void update(C32DocumentEntity document);
	
	/**
	 * @param patientId Patient ID
	 * @return A list of C32DocumentEntity's
	 */
	List<C32DocumentEntity> getAllDocuments(String patientId);
}
