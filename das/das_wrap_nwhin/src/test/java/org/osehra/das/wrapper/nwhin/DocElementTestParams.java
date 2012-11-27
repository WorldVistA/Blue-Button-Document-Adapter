package org.osehra.das.wrapper.nwhin;

public class DocElementTestParams {
	protected int daysBack = 0;
	protected int index = 999;
	
	public DocElementTestParams(int daysBack, int index) {
		super();
		this.daysBack = daysBack;
		this.index = index;
	}
	
	public int getDaysBack() {
		return daysBack;
	}
	
	public void setDaysBack(int daysBack) {
		this.daysBack = daysBack;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
}
