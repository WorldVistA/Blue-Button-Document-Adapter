package org.osehra.das.wrapper.nwhin;

public class C32DocumentEntityFactory {
	protected IC32DocumentLogic c32DocumentLogic;

	public IC32DocumentLogic getC32DocumentLogic() {
		return c32DocumentLogic;
	}

	public void setC32DocumentLogic(IC32DocumentLogic c32DocumentLogic) {
		this.c32DocumentLogic = c32DocumentLogic;
	}

	public C32DocumentEntity createDocument(String patientId, String xml) {
		return new C32DocumentEntity(patientId, getC32DocumentLogic().getPatientId(xml), new java.sql.Date(new java.util.Date().getTime()), xml);
	}
	
}
