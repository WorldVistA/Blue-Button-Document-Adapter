package org.osehra.das.repo.bluebutton;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osehra.das.wrapper.nwhin.C32DocumentEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class C32DocumentDaoImpl implements C32DocumentDao {
	protected static final Log logger = LogFactory.getLog(C32DocumentDaoImpl.class);
	@PersistenceUnit(unitName="c32")
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(C32DocumentEntity document) {
		if (logger.isDebugEnabled()) {
			logger.debug("attempting persist for: " + document);
		}
		entityManager.persist(document);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(C32DocumentEntity document) {
		if (logger.isDebugEnabled()) {
			logger.debug("updating document with:" + document);
		}
		entityManager.merge(document);
	}
	
    @SuppressWarnings("unchecked")
	@Transactional
    public List<C32DocumentEntity> getAllDocuments(String patientId) {
    	if (logger.isDebugEnabled()) {
    		logger.debug("getAllDocuments with :" + patientId);
    	}
        Query query = entityManager.createQuery("SELECT d FROM C32DocumentEntity d WHERE d.icn = :icn");
        query.setParameter("icn", patientId);
        return (List<C32DocumentEntity>) query.getResultList();
    }
    
}
