package org.osehra.das.repo.bluebutton;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osehra.das.wrapper.nwhin.C32DocumentEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements a <code>C32DocumentDao</code>, using JPA.
 * 
 * @author Dept of VA
 *
 */
public class C32DocumentDaoImpl implements C32DocumentDao {
	protected static final Log logger = LogFactory.getLog(C32DocumentDaoImpl.class);
	@PersistenceContext(unitName="c32")
	private EntityManager entityManager;
	
	/**
	 * 
	 * @return JPA entity manager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * 
	 * @param entityManager JPA entity manager
	 */
	public void setEntityManager(EntityManager manager) {
		this.entityManager = manager;
	}

	/**
	 * Delegates action to a JPA entity manager's <code>persist</code> method. 
	 */
	@Transactional
	public void insert(C32DocumentEntity document) {
		if (logger.isDebugEnabled()) {
			logger.debug("attempting persist for: " + document);
		}
		getEntityManager().persist(document);
	}

	/**
	 * Delegates action to a JPA entity manager's <code>merge</code> method.
	 */
	@Transactional
	public void update(C32DocumentEntity document) {
		if (logger.isDebugEnabled()) {
			logger.debug("updating document with:" + document);
		}
		getEntityManager().merge(document);
	}
	
	/**
	 * Selects and returns a list of <code>C32DocumentEntity</code>s using a 
	 * JPA <code>Query</code>
	 * 
	 * @param patientId
	 * @return List of <code>C32DocumentEntity</code>s matching the patientId
	 */
	@SuppressWarnings("unchecked")
	@Transactional
    public List<C32DocumentEntity> getAllDocuments(String patientId) {
    	if (logger.isDebugEnabled()) {
    		logger.debug("getAllDocuments with :" + patientId);
    	}
        Query query = getEntityManager().createQuery("SELECT d FROM C32DocumentEntity d WHERE d.icn = :icn");
        query.setParameter("icn", patientId);
        return (List<C32DocumentEntity>) query.getResultList();
    }
    
}
