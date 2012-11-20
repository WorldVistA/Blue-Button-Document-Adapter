package org.osehra.das.repo.bluebutton;

public abstract class AbstractC32DaoAware {
	protected IC32DocumentDao c32DocumentDao;
	
	public IC32DocumentDao getC32DocumentDao() {
		return c32DocumentDao;
	}

	public void setC32DocumentDao(IC32DocumentDao c32DocumentDao) {
		this.c32DocumentDao = c32DocumentDao;
	}

}
