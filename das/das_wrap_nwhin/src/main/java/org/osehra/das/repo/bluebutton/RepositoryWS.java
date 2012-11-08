package org.osehra.das.repo.bluebutton;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.osehra.das.C32Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebService(name="blueButtonRepository", targetNamespace="org.osehra.repo.bluebutton", serviceName="blueButtonRepository")
public class RepositoryWS extends SpringBeanAutowiringSupport implements Repository {
	@Autowired
	protected Repository _repo;

	@Override
	@WebMethod
	public List<DocStatus> getStatus(String patientId) {
		return _repo.getStatus(patientId);
	}

	@Override
	@WebMethod
	public C32Document getDocument(Date docDate, String patientId) {
		return _repo.getDocument(docDate, patientId);
	}

}
