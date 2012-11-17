package org.osehra.das.repo.bluebutton;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osehra.das.wrapper.nwhin.C32DocumentEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class C32DocumentDao {
	Log logger = LogFactory.getLog(this.getClass());
	
	@PersistenceUnit(unitName="c32")
	private EntityManager entityManager;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public C32DocumentEntity createAndPersistC32(String patientId, String c32) {
		logger.error("attempting persist for: " +patientId);
		C32DocumentEntity entity = new C32DocumentEntity();
		entity.setDocument(c32);
		entity.setIcn(patientId);
		entity.setDocumentPatientId(c32);
		
		Date uDate = new java.util.Date();
		entity.setCreateDate(new java.sql.Date(uDate.getTime()));
		
		entityManager.persist(entity);
		return entity;
	}
	
    @Transactional
    public List<C32DocumentEntity> getAllDocuments(String patientId) {
        Query query = entityManager.createQuery("SELECT d FROM C32DocumentEntity d WHERE d.icn = :icn");
        query.setParameter("icn", patientId);
        return (List<C32DocumentEntity>) query.getResultList();
    }
}
