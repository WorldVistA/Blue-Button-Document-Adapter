package org.osehra.das.repo.bluebutton;

/**
 * Common abstract class for classes that use a 
 * <code>org.osehra.das.repo.bluebutton.C32DocumentDao</code>.
 *  
 * @author Dept of VA
 *
 */
public abstract class AbstractC32DaoAware {
	protected C32DocumentDao c32DocumentDao;
	
	/**
	 * @return Document DAO
	 */
	public C32DocumentDao getC32DocumentDao() {
		return c32DocumentDao;
	}

	/**
	 * @param c32DocumentDao Document DAO
	 */
	public void setC32DocumentDao(C32DocumentDao c32DocumentDao) {
		this.c32DocumentDao = c32DocumentDao;
	}

}
