package org.osehra.das.repo.bluebutton;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.osehra.das.wrapper.nwhin.C32DocumentEntity;

public class C32DocumentDaoImplTest extends AbstractDateAwareTests implements EntityManager {
	C32DocumentDaoImpl dao;
	@SuppressWarnings("rawtypes")
	List persistList;

	@SuppressWarnings("rawtypes")
	@Before
	public void setup() {
		dao = new C32DocumentDaoImpl();
		dao.setEntityManager(this);
		persistList = new ArrayList();
	}

	@Test
	public void insert() {
		java.util.Date now = new java.util.Date();
		dao.insert(new C32DocumentEntity("1234", "1234", new java.sql.Timestamp(now.getTime()), "<hi></hi>"));
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
	
	/*
	 * EntityManager interface methods
	 */
	
	@Override
	public void clear() {
	}

	@Override
	public void close() {
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
	public Query createQuery(String arg0) {
		return null;
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
}
