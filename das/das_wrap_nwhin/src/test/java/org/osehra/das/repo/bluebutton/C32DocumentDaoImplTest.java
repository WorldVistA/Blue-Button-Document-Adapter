package org.osehra.das.repo.bluebutton;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.osehra.das.wrapper.nwhin.C32DocumentEntity;

public class C32DocumentDaoImplTest extends AbstractDateAwareTests implements EntityManager, EntityManagerFactory {
	C32DocumentDaoImpl dao;
	@SuppressWarnings("rawtypes")
	List persistList;
	List<java.util.Date> closeList;
	boolean factoryError = false;
	boolean managerError = false;
	TestQuery lastQuery;

	@SuppressWarnings("rawtypes")
	@Before
	public void setup() {
		dao = new C32DocumentDaoImpl();
		dao.setEntityManagerFactory(this);
		persistList = new ArrayList();
		closeList = new ArrayList<java.util.Date>();
		factoryError = false;
		managerError = false;
		lastQuery = null;
	}

	@Test
	public void insert() {
		java.util.Date now = new java.util.Date();
		dao.insert(getNewC32Doc(now));
		Assert.assertEquals(1, closeList.size());
		Assert.assertEquals(1, persistList.size());
		C32DocumentEntity aDoc = (C32DocumentEntity)persistList.get(0);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(now);
		assertDateEquals(aDoc.getCreateDate(), 
				cal.get(GregorianCalendar.YEAR), 
				cal.get(GregorianCalendar.MONTH), 
				cal.get(GregorianCalendar.DAY_OF_MONTH), 
				cal.get(GregorianCalendar.HOUR_OF_DAY), 
				cal.get(GregorianCalendar.MINUTE), 
				cal.get(GregorianCalendar.SECOND));
		Assert.assertFalse(cal.get(GregorianCalendar.HOUR_OF_DAY)==0 &&
				cal.get(GregorianCalendar.MINUTE)==0 &&
				cal.get(GregorianCalendar.SECOND)==0);
	}
	
	@Test
	public void insert_factoryError() {
		java.util.Date now = new java.util.Date();
		factoryError = true;
		try {
			dao.insert(getNewC32Doc(now));
			Assert.fail("no error thrown");
		}
		catch (RuntimeException ex) {}
		Assert.assertEquals(0, closeList.size());
	}
	
	@Test
	public void insert_managerError() {
		java.util.Date now = new java.util.Date();
		managerError = true;
		try {
			dao.insert(getNewC32Doc(now));
			Assert.fail("error not thrown");
		}
		catch (RuntimeException ex) {}
		Assert.assertEquals(1, closeList.size());
	}
	
	@Test
	public void getAllDocuments_empty() {
		List<C32DocumentEntity> results = dao.getAllDocuments("fred");
		Assert.assertNull(results);
		assertQueryTried();
	}
	
	@Test
	public void getAllDocuments_factoryError() {
		factoryError = true;
		try {
			dao.getAllDocuments("fred");
			Assert.fail("error not thrown");
		}
		catch (RuntimeException ex) {}
		Assert.assertEquals(0, closeList.size());
		Assert.assertNull(lastQuery);
	}

	@Test
	public void getAllDocuments_queryError() {
		managerError = true;
		try {
			dao.getAllDocuments("fred");
			Assert.fail("error not thrown");
		}
		catch (RuntimeException ex) {}
		Assert.assertEquals(1, closeList.size());
		assertQueryTried();
	}
	
	protected void assertQueryTried() {
		Assert.assertNotNull(lastQuery);
		Assert.assertEquals(1, lastQuery.getStringParams().size());
		Assert.assertEquals("fred", lastQuery.getStringParams().get("icn"));
		Assert.assertEquals("SELECT d FROM C32DocumentEntity d WHERE d.icn = :icn", lastQuery.getQueryString());
	}
	
	protected static C32DocumentEntity getNewC32Doc(java.util.Date aDate) {
		return new C32DocumentEntity("1234", "1234", new java.sql.Timestamp(aDate.getTime()), "<hi></hi>");
	}
	
	/*
	 * EntityManager interface methods
	 */
	
	@Override
	public void clear() {
	}

	@Override
	public void close() {
		closeList.add(new java.util.Date());
	}

	@Override
	public boolean contains(Object arg0) {
		return false;
	}

	@Override
	public Query createNamedQuery(String arg0) {
		return null;
	}

	@Override
	public Query createNativeQuery(String arg0) {
		return null;
	}

	@Override
	public Query createNativeQuery(String arg0, @SuppressWarnings("rawtypes") Class arg1) {
		return null;
	}

	@Override
	public Query createNativeQuery(String arg0, String arg1) {
		return null;
	}

	@Override
	public Query createQuery(String qString) {
		lastQuery = new TestQuery(qString);
		lastQuery.setResultErrorsOut(managerError);
		return lastQuery;
	}

	@Override
	public <T> T find(Class<T> arg0, Object arg1) {
		return null;
	}

	@Override
	public void flush() {
	}

	@Override
	public Object getDelegate() {
		return null;
	}

	@Override
	public FlushModeType getFlushMode() {
		return null;
	}

	@Override
	public <T> T getReference(Class<T> arg0, Object arg1) {
		return null;
	}

	@Override
	public EntityTransaction getTransaction() {
		return null;
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public void joinTransaction() {
	}

	@Override
	public void lock(Object arg0, LockModeType arg1) {
	}

	@Override
	public <T> T merge(T arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void persist(Object item) {
		if (managerError) {
			throw new RuntimeException("ug2");
		}
		persistList.add(item);
	}

	@Override
	public void refresh(Object arg0) {
	}

	@Override
	public void remove(Object arg0) {
	}

	@Override
	public void setFlushMode(FlushModeType arg0) {
	}

	@Override
	public EntityManager createEntityManager() {
		if (factoryError) {
			throw new RuntimeException("ug");
		}
		return this;
	}

	@Override
	public EntityManager createEntityManager(@SuppressWarnings("rawtypes") Map arg0) {
		if (factoryError) {
			throw new RuntimeException("ug");
		}
		return this;
	}
}
