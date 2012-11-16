package org.osehra.das.repo.bluebutton;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osehra.das.wrapper.nwhin.C32DocumentEntity;
import org.osehra.integration.core.component.ComponentImpl;

@org.springframework.stereotype.Repository
public class C32DocumentDao extends ComponentImpl {
	Log logger = LogFactory.getLog(this.getClass());
	
	@PersistenceContext(unitName="c32")
    private EntityManagerFactory entityManagerFactory;
	
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.entityManagerFactory = emf;
    }
    
    public EntityManagerFactory getEntityManagerFactory() {
    	return entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return  entityManagerFactory.createEntityManager();
    }
    
	public C32DocumentEntity createAndPersistC32(String patientId) {
		logger.debug("attempting persist for: " +patientId);
		C32DocumentEntity entity = new C32DocumentEntity();
		String c32Document = "";
		entity.setDocument(c32Document);
		entity.setIcn(patientId);
		entity.setDocumentPatientId(c32Document);
		
		EntityManager entityManager = getEntityManager();
		entityManager.persist(entity);
		
		logger.debug("should have just persisted an entity: " + entity);
		return entity;
	}
	
    public List<C32DocumentEntity> getAllDocuments(String patientId) {
        EntityManager entityManager = getEntityManager();
        
        List<C32DocumentEntity> list = RetrieveStatusListByIcn(entityManager, patientId);
        return list ;
      }

    private List<C32DocumentEntity> RetrieveStatusListByIcn(EntityManager em, String patientId) {
      Query query = em.createQuery("SELECT d FROM C32DocumentEntity d WHERE d.icn = :icn");
      query.setParameter("icn", patientId);
      return (List<C32DocumentEntity>) query.getResultList();
    }
}
