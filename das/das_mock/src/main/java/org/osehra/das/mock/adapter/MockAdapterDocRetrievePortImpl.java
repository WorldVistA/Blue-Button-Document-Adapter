package org.osehra.das.mock.adapter;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequest;
import org.osehra.integration.util.FileUtil;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponse;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.ws.client.core.WebServiceTemplate;

public class MockAdapterDocRetrievePortImpl extends WebServiceTemplate {

	/**
	 * @uml.property name="applicationContext"
	 * @uml.associationEnd readOnly="true"
	 */
	@Autowired
	ApplicationContext applicationContext;

	/**
	 * @uml.property name="count"
	 */
	private int count = 0;
	/**
	 * @uml.property name="randomDocs"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	List<String> randomDocs;

	public MockAdapterDocRetrievePortImpl() {
	}

	@Override
	public void afterPropertiesSet() {

	}

	@Override
	protected void initDefaultStrategies() {
	}

	@Override
	public Object marshalSendAndReceive(final Object requestPayload) {
		return this
				.respondingGatewayCrossGatewayRetrieve((RespondingGatewayCrossGatewayRetrieveRequest) requestPayload);
	}

	public RetrieveDocumentSetResponse respondingGatewayCrossGatewayRetrieve(
			final RespondingGatewayCrossGatewayRetrieveRequest respondingGatewayCrossGatewayRetrieveRequest) {
		final DocumentResponse documentResponse = new DocumentResponse();
		String responseDoc = "";
		if (this.randomDocs.size() > this.count) {
			responseDoc = this.randomDocs.get(this.count);
		} else {
			this.count = 0;
			responseDoc = this.randomDocs.get(this.count);
		}
		this.count++;
		documentResponse.setDocument(responseDoc.getBytes());
		final RetrieveDocumentSetResponse retrieveDocumentSetResponse = new RetrieveDocumentSetResponse();
		retrieveDocumentSetResponse.getDocumentResponse().add(documentResponse);
		return retrieveDocumentSetResponse;
	}

	public String returnC32Response() throws IOException {
		final org.springframework.core.io.Resource resource = this.applicationContext
				.getResource("classpath:org/osehra/das/mock/endpoint/adapter/doc/Utah 2011 03 05 CCD.XML");
		final String utahC32 = FileUtil.getResource(resource);
		return utahC32;
	}

	public String returnC62Response() throws IOException {
		final org.springframework.core.io.Resource resource = this.applicationContext
				.getResource("classpath:org/osehra/das/mock/endpoint/adapter/report/Joseph Nhinpatient XDS-SD.xml");
		final String josephC62 = FileUtil.getResource(resource);
		return josephC62;
	}

	public void setRandomFiles(final List<Resource> randomFiles)
			throws IOException {
		this.randomDocs = new ArrayList<String>();
		for (final Resource res : randomFiles) {
			final String someFile = FileUtil.getResource(res);
			this.randomDocs.add(someFile);
		}

	}
}
