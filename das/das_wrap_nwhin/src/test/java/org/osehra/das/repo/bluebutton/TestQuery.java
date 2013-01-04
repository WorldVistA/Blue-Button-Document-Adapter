package org.osehra.das.repo.bluebutton;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class TestQuery implements Query {
	Map<String, Object> stringParamMap;
	String queryString;
	boolean resultErrorsOut = false;
	
	public TestQuery() {}
	
	public TestQuery(String qString) {
		this();
		this.queryString = qString;
	}
	
	public void setResultErrorsOut(boolean errsOut) {
		resultErrorsOut = errsOut;
	}
	
	public boolean isResultErrorsOut() {
		return resultErrorsOut;
	}
	
	public String getQueryString() {
		return queryString;
	}
	
	public Map<String, Object> getStringParams() {
		return stringParamMap;
	}

	@Override
	public int executeUpdate() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getResultList() {
		if (isResultErrorsOut()) {
			throw new RuntimeException("result error");
		}
		return null;
	}

	@Override
	public Object getSingleResult() {
		return null;
	}

	@Override
	public Query setFirstResult(int arg0) {
		return this;
	}

	@Override
	public Query setFlushMode(FlushModeType arg0) {
		return this;
	}

	@Override
	public Query setHint(String arg0, Object arg1) {
		return this;
	}

	@Override
	public Query setMaxResults(int arg0) {
		return this;
	}

	@Override
	public Query setParameter(String name, Object item) {
		if (stringParamMap==null) {
			stringParamMap = new HashMap<String, Object>();
		}
		stringParamMap.put(name, item);
		return this;
	}

	@Override
	public Query setParameter(int arg0, Object arg1) {
		return this;
	}

	@Override
	public Query setParameter(String arg0, Date arg1, TemporalType arg2) {
		return this;
	}

	@Override
	public Query setParameter(String arg0, Calendar arg1, TemporalType arg2) {
		return this;
	}

	@Override
	public Query setParameter(int arg0, Date arg1, TemporalType arg2) {
		return this;
	}

	@Override
	public Query setParameter(int arg0, Calendar arg1, TemporalType arg2) {
		return this;
	}

}
