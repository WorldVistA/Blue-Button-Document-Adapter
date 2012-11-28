package org.osehra.das.wrapper.nwhin;

public interface C32DocumentEntityFactory {
	C32DocumentEntity createDocument(String patientId, String xml) throws Exception;
}
