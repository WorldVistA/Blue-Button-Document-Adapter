package org.osehra.das.wrapper.nwhin;

/**
 * Interface for a delegate that returns a C32 document from the NwHIN adapter.
 * @author Dept of VA
 *
 */
public interface WrapperResource {
	
	/**
	 * 
	 * @param patientId Patient ID
	 * @param userName User name
	 * @return C32 XML document
	 */
	Object getDomainXml(String patientId, String userName);
}
