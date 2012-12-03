package org.osehra.das.repo.bluebutton;

public abstract class AbstractC32DaoAware {
	protected C32DocumentDao c32DocumentDao;
	
	public C32DocumentDao getC32DocumentDao() {
		return c32DocumentDao;
	}

	public void setC32DocumentDao(C32DocumentDao c32DocumentDao) {
		this.c32DocumentDao = c32DocumentDao;
	}

}
