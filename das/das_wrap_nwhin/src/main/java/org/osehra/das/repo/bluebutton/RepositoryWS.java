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

@WebService(targetNamespace="org.osehra.repo.bluebutton", serviceName="blueButtonRepository")
public class RepositoryWS {
	Log logger = LogFactory.getLog(this.getClass());

	@WebMethod
	public List<DocStatus> getStatus(String patientId, String patientName) {
		return getRepository().getStatus(patientId, patientName);
	}

	@WebMethod
	public C32Document getDocument(Date docDate, String patientId) {
		logger.debug("date:" + docDate + " id:" + patientId);
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
