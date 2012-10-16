package org.osehra.das.wrapper.nwhin.doc;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequest;
import org.osehra.integration.util.NullChecker;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponse;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Endpoint that talks to the adapter's document retrieve endpoint. This
 * endpoint is created using the same interfaces that the adapter uses to
 * communicate with the gateways.
 *
 * @author Asha Amritraj
 */
public class AdapterDocRetrieveEndpoint {
	private static final String DOCUMENT_CACHE_NAME = "org.osehra.nvap.server.endpoint.adapter.doc.DocumentCache";
	/**
	 * The template for calling the adapter's webservice.
	 */
	private WebServiceTemplate adapterDocRetrieveWebServiceTemplate;
	// Cache Manager
	CacheManager cacheManager;
	/**
	 * Inject VA home community name.
	 */
	private String homeCommunityName;
	/**
	 * Inject VA organization detail.
	 */
	private String homeCommunityOid;

	/**
	 * The patient demographics query service.
	private PdqService pdqService;
	 */

	public String getDocument(final String icn, final String documentUniqueId,
			final String documentRepositoryId, final String homeCommunityId,
			final String userName) {

		// Cache lives for 10 mins. Check the ehCache.xml for more
		// information.
		if (CacheManager.getInstance().cacheExists(
				AdapterDocRetrieveEndpoint.DOCUMENT_CACHE_NAME)) {
			final Cache cache = CacheManager.getInstance().getCache(
					AdapterDocRetrieveEndpoint.DOCUMENT_CACHE_NAME);
			final String key = documentUniqueId + documentRepositoryId
					+ homeCommunityId;
			if (cache.isKeyInCache(key)) {
				final Element element = cache.get(key);
				if (NullChecker.isNotEmpty(element)) {
					return (String) element.getObjectValue();
				}
			}
		}

		final RespondingGatewayCrossGatewayRetrieveRequest request = new RespondingGatewayCrossGatewayRetrieveRequest();
		final ihe.iti.xds_b._2007.ObjectFactory objectFactory = new ihe.iti.xds_b._2007.ObjectFactory();
		final RetrieveDocumentSetRequestType retrieveRequest = objectFactory
				.createRetrieveDocumentSetRequestType();
		request.setRetrieveDocumentSetRequest(retrieveRequest);

		// Get Patient Demographics
		//final PatientDemographics patientDemographics = this
		//		.getPatientDemographics(icn);

		final AssertionType assertionValue = new AssertionType();

		final gov.hhs.fha.nhinc.common.nhinccommon.AddressType addressValue = new gov.hhs.fha.nhinc.common.nhinccommon.AddressType();
		final gov.hhs.fha.nhinc.common.nhinccommon.CeType addressTypeCode = new gov.hhs.fha.nhinc.common.nhinccommon.CeType();
		addressTypeCode.setCode("H");
		addressValue.setAddressType(addressTypeCode);
		//addressValue.setCity(patientDemographics.getResidenceCity());
		addressValue.setCountry("USA");
		//addressValue.setState(patientDemographics.getResidenceState());
		//addressValue.setStreetAddress(patientDemographics
		//		.getStreetAddressLine1());
		//addressValue.setZipCode(patientDemographics.getResidenceZip4());
		assertionValue.setAddress(addressValue);

		final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		final Calendar cal = Calendar.getInstance();

		//assertionValue.setDateOfBirth(formatter.format(patientDemographics
		//		.getDob()));
		cal.add(Calendar.YEAR, 5);
		assertionValue.setExplanationNonClaimantSignature("NEEDED");
		assertionValue.setHaveSecondWitnessSignature(false);
		assertionValue.setHaveSignature(true);
		assertionValue.setHaveWitnessSignature(false);

		final HomeCommunityType homeCommunityType = new HomeCommunityType();
		homeCommunityType.setHomeCommunityId(this.homeCommunityOid);
		homeCommunityType.setName(this.homeCommunityName);
		assertionValue.setHomeCommunity(homeCommunityType);

		PersonNameType personNameType = new PersonNameType();
		//personNameType.setFamilyName(patientDemographics.getLastName());
		//personNameType.setGivenName(patientDemographics.getFirstName());
		final CeType nameType = new CeType();
		nameType.setCode("G");
		personNameType.setNameType(nameType);
		assertionValue.setPersonName(personNameType);

		final gov.hhs.fha.nhinc.common.nhinccommon.PhoneType phoneType = new gov.hhs.fha.nhinc.common.nhinccommon.PhoneType();
		phoneType.setAreaCode("703");
		phoneType.setCountryCode("1");
		phoneType.setExtension("1212");
		phoneType.setLocalNumber("555");
		final CeType phoneNumberType = new CeType();
		phoneNumberType.setCode("W");
		phoneType.setPhoneNumberType(phoneNumberType);
		assertionValue.setPhoneNumber(phoneType);

		final UserType userType = new UserType();
		personNameType = new PersonNameType();
		personNameType.setFamilyName("");
		personNameType.setGivenName("");
		personNameType.setSecondNameOrInitials("");
		nameType.setCode("P");
		userType.setPersonName(personNameType);
		userType.setUserName(userName);
		personNameType.setNameType(nameType);

		userType.setOrg(homeCommunityType);

		final gov.hhs.fha.nhinc.common.nhinccommon.CeType roleCoded = new gov.hhs.fha.nhinc.common.nhinccommon.CeType();
		roleCoded.setCode("307969004");
		roleCoded.setCodeSystem("2.16.840.1.113883.3.18.7.1");
		roleCoded.setCodeSystemName("SNOMED_CT");
		roleCoded.setCodeSystemVersion("1.0");
		roleCoded.setDisplayName("Public Health");
		roleCoded.setOriginalText("Public Health");
		userType.setRoleCoded(roleCoded);

		assertionValue.setUserInfo(userType);

		final gov.hhs.fha.nhinc.common.nhinccommon.CeType purposeOfDiscolureCoded = new gov.hhs.fha.nhinc.common.nhinccommon.CeType();
		purposeOfDiscolureCoded.setCode("TREATMENT");
		purposeOfDiscolureCoded.setCodeSystem("2.16.840.1.113883.3.18.7.1");
		purposeOfDiscolureCoded.setCodeSystemName("nhin-purpose");
		purposeOfDiscolureCoded.setCodeSystemVersion("1.0");
		purposeOfDiscolureCoded.setDisplayName("Use or disclosure of");
		purposeOfDiscolureCoded.setOriginalText("notes");
		assertionValue.setPurposeOfDisclosureCoded(purposeOfDiscolureCoded);
		//assertionValue.setSSN(patientDemographics.getSsn());

		request.setAssertion(assertionValue);

		final DocumentRequest documentRequest = new DocumentRequest();
		documentRequest.setDocumentUniqueId(documentUniqueId);
		documentRequest.setHomeCommunityId(homeCommunityId);
		documentRequest.setRepositoryUniqueId(documentRepositoryId);
		retrieveRequest.getDocumentRequest().add(documentRequest);
		// Send request using Spring-WS
		final RetrieveDocumentSetResponse response = (RetrieveDocumentSetResponse) this.adapterDocRetrieveWebServiceTemplate
				.marshalSendAndReceive(request);
		if (NullChecker.isNotEmpty(response)) {
			final List<DocumentResponse> documentResponses = response
					.getDocumentResponse();
			if (NullChecker.isNotEmpty(documentResponses)) {
				final DocumentResponse docResponse = documentResponses.get(0);
				if (NullChecker.isNotEmpty(docResponse)) {
					// Note that JAXB unmarshaller would decode the message
					final byte[] doc = docResponse.getDocument();
					final String document = new String(doc);

					// Store in cache
					if (CacheManager.getInstance().cacheExists(
							AdapterDocRetrieveEndpoint.DOCUMENT_CACHE_NAME)) {
						final String key = documentUniqueId
								+ documentRepositoryId + homeCommunityId;
						final Cache cache = CacheManager
								.getInstance()
								.getCache(
										AdapterDocRetrieveEndpoint.DOCUMENT_CACHE_NAME);
						cache.put(new Element(key, document));
					}
					// Return response
					return document;
				}
			}
		}
		// Return empty document if response is empty
		return null;
	}
	@Required
	public void setAdapterDocRetrieveWebServiceTemplate(
			final WebServiceTemplate adapterDocRetrieveWebServiceTemplate) {
		this.adapterDocRetrieveWebServiceTemplate = adapterDocRetrieveWebServiceTemplate;
	}

	@Required
	public void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Required
	public void setHomeCommunityName(final String homeCommunityName) {
		this.homeCommunityName = homeCommunityName;
	}

	@Required
	public void setHomeCommunityOid(final String homeCommunityOid) {
		this.homeCommunityOid = homeCommunityOid;
	}

/*
	@Required
	public void setPdqService(final PdqService pdqService) {
		this.pdqService = pdqService;
	}
*/
}
