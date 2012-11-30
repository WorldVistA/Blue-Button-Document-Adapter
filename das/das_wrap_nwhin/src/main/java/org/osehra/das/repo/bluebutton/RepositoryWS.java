package org.osehra.das.repo.bluebutton;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osehra.das.C32Document;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * Public facing web services package.
 * 
 * @author Steve Monson
 *
 */

@WebService(targetNamespace="org.osehra.repo.bluebutton", serviceName="blueButtonRepository")
public class RepositoryWS {
	protected static final Log logger = LogFactory.getLog(RepositoryWS.class);

	/**
	 * Web Service takes two parameters and returns a list of available documents in the repository.
	 * 
	 * @param patientId    	Patient identifier of the individual.
	 * @param patientName  	Last name of the associated patient.
	 * @return 				An XML response indicating record date and status.
	 */
	@WebMethod
	public List<DocStatus> getStatus(String patientId, String patientName) {
		return getRepository().getStatus(patientId, patientName);
	}
	
	/**
	 * Web Service takes two parameters and returns documents when available.
	 * 
	 * @param docDate		Date of the requested document.
	 * @param patientId		Patient identifier of the individual.
	 * @return				An XML response containing the either date and document in BASE64 encoding, or date and error message.
	 */
	@WebMethod
	public C32Document getDocument(Date docDate, String patientId) {
		return getRepository().getDocument(docDate, patientId);
	}
	
	protected Repository getRepository() {
		WebApplicationContext cc = ContextLoader.getCurrentWebApplicationContext();
		Repository _repoImpl = cc.getBean(Repository.class);
		if (_repoImpl==null) {
			logger.warn("repoImpl is null");
		}
		return _repoImpl;
	}
	
}
