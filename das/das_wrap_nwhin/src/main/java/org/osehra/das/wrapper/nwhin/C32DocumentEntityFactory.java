package org.osehra.das.wrapper.nwhin;

/**
 * Interface for a factory to create a 
 * <code>org.osehra.das.wrapper.nwhin.C32DocumentEntity</code>
 * from an xml string.<p>
 * Implementations should set the current data/time as the 
 * <code>creationDate</code> property in the returned object, should set the 
 * <code>documentPatientId</code> property from the patient ID found in the xml 
 * string, and can filter the C32 XML document before it's stored.
 * @author Dept of VA
 *
 */
public interface C32DocumentEntityFactory {
	C32DocumentEntity createDocument(String patientId, String xml) throws Exception;
}
