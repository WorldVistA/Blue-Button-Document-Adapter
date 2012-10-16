package org.osehra.das.mock.adapter;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequest;
import org.osehra.integration.util.NullChecker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ws.client.core.WebServiceTemplate;

public class MockAdapterDocQueryPortImpl extends WebServiceTemplate {

	static private final String DATE_FORMAT_CREATION = MockAdapterDocQueryPortImpl.DATE_FORMAT_FULL;
	static private final String DATE_FORMAT_FULL = "yyyyMMddHHmmssZ";
	static private final String DATE_FORMAT_SERVICE = "yyyyMMdd";
	static private final String EBXML_DOCENTRY_CLASS_CODE = "$XDSDocumentEntryClassCode";
	static private final String EBXML_DOCENTRY_CLASS_CODE_SCHEME = "$XDSDocumentEntryClassCodeScheme";
	static private final String EBXML_DOCENTRY_PATIENT_ID = "$XDSDocumentEntryPatientId";
	static private final String EBXML_DOCENTRY_SERVICE_START_TIME_FROM = "$XDSDocumentEntryServiceStartTimeFrom";
	static private final String EBXML_DOCENTRY_SERVICE_STOP_TIME_TO = "$XDSDocumentEntryServiceStopTimeTo";
	static private final String EBXML_RESPONSE_AUTHOR_CLASS_SCHEME = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
	static private final String EBXML_RESPONSE_AUTHOR_INSTITUTION_SLOTNAME = "authorInstitution";
	static private final String EBXML_RESPONSE_CLASSCODE_CLASS_SCHEME = "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";
	static private final String EBXML_RESPONSE_CODE_CODESCHEME_SLOTNAME = "codingScheme";
	static private final String EBXML_RESPONSE_CREATIONTIME_SLOTNAME = "creationTime";
	static private final String EBXML_RESPONSE_DOCID_IDENTIFICATION_SCHEME = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
	static private final String EBXML_RESPONSE_DOCID_NAME = "XDSDocumentEntry.uniqueId";
	static private final String EBXML_RESPONSE_HASH_SLOTNAME = "hash";
	static private final String EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME = "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";
	static private final String EBXML_RESPONSE_PATIENTID_NAME = "XDSDocumentEntry.patientId";
	// We need to be able to do a search using AdhocQueryRequest parameters, but
	// XDS.b does not have a search parameter slot name defined for repository
	// ID and document ID. So we had to create our own custom ones.
	// ----------------------------------------------------------------------------
	static private final String EBXML_RESPONSE_REPOSITORY_UNIQUE_ID_SLOTNAME = "repositoryUniqueId";
	static private final String EBXML_RESPONSE_SERVICESTARTTIME_SLOTNAME = "serviceStartTime";
	static private final String EBXML_RESPONSE_SERVICESTOPTIME_SLOTNAME = "serviceStopTime";
	static private final String EBXML_RESPONSE_SIZE_SLOTNAME = "size";
	static private final Log LOG = LogFactory
			.getLog(MockAdapterDocQueryPortImpl.class.getName());
	static private final String REPOSITORY_UNIQUE_ID = "1";
	// Properties file keys
	static private final long TWO_YEARS = 60 * 60 * 24 * 365 * 2 * 1000L;
	static private final String XDS_QUERY_RESPONSE_EXTRINSIC_OBJCECT_OBJECT_TYPE = "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";
	static private final String XDS_QUERY_RESPONSE_STATUS_FAILURE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";

	static private final String XDS_QUERY_RESPONSE_STATUS_SUCCESS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";

	@Override
	public void afterPropertiesSet() {
	}

	private AdhocQueryResponse createAdhocQueryResponse(
			final AdhocQueryRequest adhocQueryRequest, final List<Document> docs) {
		final oasis.names.tc.ebxml_regrep.xsd.query._3.ObjectFactory retObjFactory = new oasis.names.tc.ebxml_regrep.xsd.query._3.ObjectFactory();
		final AdhocQueryResponse ret = retObjFactory.createAdhocQueryResponse();
		ret.setStatus(MockAdapterDocQueryPortImpl.XDS_QUERY_RESPONSE_STATUS_SUCCESS);
		ret.setRequestId(adhocQueryRequest.getId());
		final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
		final RegistryObjectListType regObjList = new RegistryObjectListType();
		ret.setRegistryObjectList(regObjList);
		// Collect the home community id
		final String homeCommunityId = this.retrieveHomeCommunityId();
		if ((docs != null) && (!docs.isEmpty())) {
			final List<JAXBElement<? extends IdentifiableType>> olRegObjs = regObjList
					.getIdentifiable();
			// Save these so that theyu can be added in later after all of the
			// other items..
			// ------------------------------------------------------------------------------
			final ArrayList<JAXBElement<? extends IdentifiableType>> olObjRef = new ArrayList<JAXBElement<? extends IdentifiableType>>();
			final ArrayList<JAXBElement<? extends IdentifiableType>> olAssoc = new ArrayList<JAXBElement<? extends IdentifiableType>>();
			for (final Document doc : docs) {
				final ExtrinsicObjectType oExtObj = new ExtrinsicObjectType();
				final JAXBElement<? extends IdentifiableType> oJAXBExtObj = oRimObjectFactory
						.createExtrinsicObject(oExtObj);
				final List<SlotType1> olSlot = oExtObj.getSlot();
				final List<ClassificationType> olClassifications = oExtObj
						.getClassification();
				boolean bHaveData = false;
				oExtObj.setIsOpaque(Boolean.FALSE);
				oExtObj.setObjectType(MockAdapterDocQueryPortImpl.XDS_QUERY_RESPONSE_EXTRINSIC_OBJCECT_OBJECT_TYPE);
				// Generate a UUID for the document
				final UUID oDocumentUUID = UUID.randomUUID();
				final String sDocumentUUID = "urn:uuid:"
						+ oDocumentUUID.toString();
				oExtObj.setId(sDocumentUUID);
				// Document Unique ID
				// ------------------
				String sDocumentId = ""; // need to keep a handle to this to
				// be used later...
				if (!NullChecker.isNullOrEmpty(doc.getDocumentUniqueId())) {
					sDocumentId = doc.getDocumentUniqueId();
					final ExternalIdentifierType oExtId = new ExternalIdentifierType();
					oExtId.setId("urn:uuid:" + UUID.randomUUID().toString());
					oExtObj.getExternalIdentifier().add(oExtId);
					oExtId.setRegistryObject(sDocumentUUID);
					oExtId.setValue(sDocumentId);
					oExtId.setIdentificationScheme(MockAdapterDocQueryPortImpl.EBXML_RESPONSE_DOCID_IDENTIFICATION_SCHEME);
					final InternationalStringType oName = this
							.CreateSingleValueInternationalStringType(MockAdapterDocQueryPortImpl.EBXML_RESPONSE_DOCID_NAME);
					oExtId.setName(oName);
					bHaveData = true;
				}
				// Author data
				boolean bHasAuthorData = false;
				final ClassificationType oClassification = new ClassificationType();
				oClassification.setId("urn:uuid:"
						+ UUID.randomUUID().toString());
				oClassification
						.setClassificationScheme(MockAdapterDocQueryPortImpl.EBXML_RESPONSE_AUTHOR_CLASS_SCHEME);
				oClassification.setClassifiedObject(sDocumentUUID);
				oClassification.setNodeRepresentation("");
				final List<SlotType1> olClassificationSlot = oClassification
						.getSlot();
				// TODO:AuthorPerson
				// -------------
				/*
				 * if(!NullChecker.isNullOrEmpty(doc.getAuthorPerson())) {
				 * SlotType1 oSlot =
				 * CreateSingleValueSlot(EBXML_RESPONSE_AUTHOR_PERSON_SLOTNAME,
				 * doc.getAuthorPerson()); olClassificationSlot.add(oSlot);
				 * bHasAuthorData = true; }
				 */
				// Author institution
				// ------------------------------------------------------

				final SlotType1 oxSlot = this
						.CreateSingleValueSlot(
								MockAdapterDocQueryPortImpl.EBXML_RESPONSE_AUTHOR_INSTITUTION_SLOTNAME,
								"Department of Veterans Affairs");
				olClassificationSlot.add(oxSlot);
				bHasAuthorData = true;
				// TODO: AuthorRole
				// ------------
				/*
				 * if(!NullChecker.isNullOrEmpty(doc.getAuthorRole())) {
				 * SlotType1 oSlot =
				 * CreateSingleValueSlot(EBXML_RESPONSE_AUTHOR_ROLE_SLOTNAME,
				 * doc.getAuthorRole()); olClassificationSlot.add(oSlot);
				 * bHasAuthorData = true; }
				 */
				// TODO: AuthorSpecialty
				// ----------------
				/*
				 * if(!NullChecker.isNullOrEmpty(doc.getAuthorSpecialty())) {
				 * SlotType1 oSlot =
				 * CreateSingleValueSlot(EBXML_RESPONSE_AUTHOR_SPECIALTY_SLOTNAME
				 * , doc.getAuthorSpecialty()); olClassificationSlot.add(oSlot);
				 * bHasAuthorData = true; }
				 */
				if (bHasAuthorData) {
					olClassifications.add(oClassification);
					bHaveData = true;
				}
				// TODO: Availability Status
				// ---------------------
				/*
				 * if(!NullChecker.isNullOrEmpty(doc.getAvailablityStatus())) {
				 * oExtObj.setStatus(doc.getAvailablityStatus()); bHaveData =
				 * true; }
				 */
				// Class Code
				// ------------
				final ClassificationType classCodeClassification = this
						.createClassificationFromCodedData(
								doc.getClassCode(),
								doc.getClassCodeScheme(),
								doc.getClassCodeDisplayName(),
								MockAdapterDocQueryPortImpl.EBXML_RESPONSE_CLASSCODE_CLASS_SCHEME,
								sDocumentUUID);
				if (classCodeClassification != null) {
					olClassifications.add(classCodeClassification);
					bHaveData = true;
				}
				// TODO: Comments
				// ---------
				/*
				 * if(!NullChecker.isNullOrEmpty(doc.getComments())) {
				 * InternationalStringType oComments =
				 * CreateSingleValueInternationalStringType(doc.getComments());
				 * oExtObj.setDescription(oComments); bHaveData = true; }
				 */
				// TODO: Confidentiality Code
				// ---------------------
				/*
				 * ClassificationType confidentialityCodeClassification =
				 * createClassificationFromCodedData
				 * (doc.getConfidentialityCode(),
				 * doc.getConfidentialityCodeScheme(),
				 * doc.getConfidentialityCodeDisplayName(),
				 * EBXML_RESPONSE_CONFIDENTIALITYCODE_CLASS_SCHEME,
				 * sDocumentUUID); if (confidentialityCodeClassification !=
				 * null) {
				 * olClassifications.add(confidentialityCodeClassification);
				 * bHaveData = true; }
				 */
				// Creation Time
				// --------------
				if (doc.getCreationTime() != null) {
					final SlotType1 oSlot = this
							.CreateSingleValueSlot(
									MockAdapterDocQueryPortImpl.EBXML_RESPONSE_CREATIONTIME_SLOTNAME,
									this.formatCreationDate(doc
											.getCreationTime()));
					olSlot.add(oSlot);
					bHaveData = true;
				}
				// TODO: Event Code List
				// ----------------
				/*
				 * if((doc.getEventCodes() != null) &&
				 * (!doc.getEventCodes().isEmpty())) { for(EventCode eventCode :
				 * doc.getEventCodes()) { ClassificationType
				 * eventCodeClassification =
				 * createClassificationFromCodedData(eventCode.getEventCode(),
				 * eventCode.getEventCodeScheme(),
				 * eventCode.getEventCodeDisplayName(),
				 * EBXML_RESPONSE_EVENTCODE_CLASS_SCHEME, sDocumentUUID); if
				 * (eventCodeClassification != null) {
				 * olClassifications.add(eventCodeClassification); bHaveData =
				 * true; } } }
				 */
				// TODO: Format Code
				// -------------
				/*
				 * ClassificationType formatCodeClassification =
				 * createClassificationFromCodedData(doc.getFormatCode(),
				 * doc.getFormatCodeScheme(), doc.getFormatCodeDisplayName(),
				 * EBXML_RESPONSE_FORMATCODE_CLASS_SCHEME, sDocumentUUID); if
				 * (formatCodeClassification != null) {
				 * olClassifications.add(formatCodeClassification); bHaveData =
				 * true; }
				 */
				// Hash Code
				// ----------
				if (!NullChecker.isNullOrEmpty(doc.getHash())) {
					final SlotType1 oSlot = this
							.CreateSingleValueSlot(
									MockAdapterDocQueryPortImpl.EBXML_RESPONSE_HASH_SLOTNAME,
									doc.getHash());
					olSlot.add(oSlot);
					bHaveData = true;
				}
				// TODO: Healthcare Facility Type Code
				// ------------------------------
				/*
				 * ClassificationType healthcareFacilityTypeCodeClassification =
				 * createClassificationFromCodedData(doc.getFacilityCode(),
				 * doc.getFacilityCodeScheme(),
				 * doc.getFacilityCodeDisplayName(),
				 * EBXML_RESPONSE_HEALTHCAREFACILITYTYPE_CLASS_SCHEME,
				 * sDocumentUUID); if (healthcareFacilityTypeCodeClassification
				 * != null) {
				 * olClassifications.add(healthcareFacilityTypeCodeClassification
				 * ); bHaveData = true; }
				 */
				// TODO: Intended Recipients
				// --------------------
				/*
				 * List<String> intendedRecipients = new ArrayList<String>();
				 * if(
				 * !NullChecker.isNullOrEmpty(doc.getIntendedRecipientPerson()
				 * )) {
				 * intendedRecipients.add(doc.getIntendedRecipientPerson()); }
				 * else if(!NullChecker.isNullOrEmpty(doc.
				 * getIntendedRecipientOrganization())) {
				 * intendedRecipients.add(
				 * doc.getIntendedRecipientOrganization()) ; }
				 * if(!intendedRecipients.isEmpty()) { String[]
				 * intendedRecipientArray = intendedRecipients.toArray(new
				 * String[intendedRecipients.size()]); SlotType1 oSlot =
				 * CreateMultiValueSlot
				 * (EBXML_RESPONSE_INTENDEDRECIPIENTS_SLOTNAME ,
				 * intendedRecipientArray); olSlot.add(oSlot); bHaveData = true;
				 * }
				 */
				// TODO: Language Code
				// ---------------
				/*
				 * if(!NullChecker.isNullOrEmpty(doc.getLanguageCode())) {
				 * SlotType1 oSlot =
				 * CreateSingleValueSlot(EBXML_RESPONSE_LANGUAGECODE_SLOTNAME,
				 * doc.getLanguageCode()); olSlot.add(oSlot); bHaveData = true;
				 * }
				 */
				// TODO: LegalAuthenticator Code
				// ------------------------
				/*
				 * if(!NullChecker.isNullOrEmpty(doc.getLegalAuthenticator())) {
				 * SlotType1 oSlot =
				 * CreateSingleValueSlot(EBXML_RESPONSE_LEGALAUTHENTICATOR_SLOTNAME
				 * , doc.getLegalAuthenticator()); olSlot.add(oSlot); bHaveData
				 * = true; }
				 */
				// Mime Type
				// ----------
				if (!NullChecker.isNullOrEmpty(doc.getMimeType())) {

					oExtObj.setMimeType(doc.getMimeType());

					bHaveData = true;
				}
				// Patient ID
				// -----------
				if (!NullChecker.isNullOrEmpty(doc.getPatientId())) {
					final String formattedPatientId = doc.getPatientId()
							+ "^^^&" + homeCommunityId + "&ISO";// PatientIdFormatUtil.hl7EncodePatientId(doc.getPatientId(),
					// homeCommunityId);
					final ExternalIdentifierType oExtId = new ExternalIdentifierType();
					oExtId.setId("urn:uuid:" + UUID.randomUUID().toString());
					oExtId.setIdentificationScheme(MockAdapterDocQueryPortImpl.EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME);
					final InternationalStringType oPatIdName = this
							.CreateSingleValueInternationalStringType(MockAdapterDocQueryPortImpl.EBXML_RESPONSE_PATIENTID_NAME);
					oExtId.setName(oPatIdName);
					oExtId.setRegistryObject(sDocumentUUID);
					oExtId.setValue(formattedPatientId);
					oExtObj.getExternalIdentifier().add(oExtId);
					bHaveData = true;
				}
				// TODO: Practice Setting Code
				// ----------------------
				/*
				 * ClassificationType practiceSettingCodeClassification =
				 * createClassificationFromCodedData(doc.getPracticeSetting(),
				 * doc.getPracticeSettingScheme(),
				 * doc.getPracticeSettingDisplayName(),
				 * EBXML_RESPONSE_PRACTICESETTING_CLASS_SCHEME, sDocumentUUID);
				 * if (practiceSettingCodeClassification != null) {
				 * olClassifications.add(practiceSettingCodeClassification);
				 * bHaveData = true; }
				 */
				// Service Start Time
				// -------------------
				if (doc.getBeginDate() != null) {
					final SlotType1 oSlot = this
							.CreateSingleValueSlot(
									MockAdapterDocQueryPortImpl.EBXML_RESPONSE_SERVICESTARTTIME_SLOTNAME,
									this.formatServiceDate(doc.getBeginDate()));
					olSlot.add(oSlot);
					bHaveData = true;
				}
				// Service Stop Time
				// ------------------
				if (doc.getEndDate() != null) {
					final SlotType1 oSlot = this
							.CreateSingleValueSlot(
									MockAdapterDocQueryPortImpl.EBXML_RESPONSE_SERVICESTOPTIME_SLOTNAME,
									this.formatServiceDate(doc.getEndDate()));
					olSlot.add(oSlot);
					bHaveData = true;
				}
				// Size
				// -----
				if ((doc.getSize() != null) && (doc.getSize().intValue() > 0)) {
					final SlotType1 oSlot = this
							.CreateSingleValueSlot(
									MockAdapterDocQueryPortImpl.EBXML_RESPONSE_SIZE_SLOTNAME,
									doc.getSize().toString());
					olSlot.add(oSlot);
					bHaveData = true;
				}
				// TODO: Source Patient Id
				// ------------------
				/*
				 * if(!NullChecker.isNullOrEmpty(doc.getSourcePatientId())) {
				 * SlotType1 oSlot =
				 * CreateSingleValueSlot(EBXML_RESPONSE_SOURCEPATIENTID_SLOTNAME
				 * , doc.getSourcePatientId()); olSlot.add(oSlot); bHaveData =
				 * true; }
				 */

				// TODO: Source Patient Info
				// --------------------
				/*
				 * List<String> sourcePatientInfoValues = new
				 * ArrayList<String>();
				 * if(!NullChecker.isNullOrEmpty(doc.getPid3())) {
				 * sourcePatientInfoValues.add("PID-3|" + doc.getPid3()); }
				 * if(!NullChecker.isNullOrEmpty(doc.getPid5())) {
				 * sourcePatientInfoValues.add("PID-5|" + doc.getPid5()); }
				 * if(!NullChecker.isNullOrEmpty(doc.getPid7())) {
				 * sourcePatientInfoValues.add("PID-7|" + doc.getPid7()); }
				 * if(!NullChecker.isNullOrEmpty(doc.getPid8())) {
				 * sourcePatientInfoValues.add("PID-8|" + doc.getPid8()); }
				 * if(!NullChecker.isNullOrEmpty(doc.getPid11())) {
				 * sourcePatientInfoValues.add("PID-11|" + doc.getPid11()); }
				 * if(!sourcePatientInfoValues.isEmpty()) { String[]
				 * sourcePatientInfoValuesArray =
				 * sourcePatientInfoValues.toArray(new
				 * String[sourcePatientInfoValues.size()]); SlotType1 oSlot =
				 * CreateMultiValueSlot
				 * (EBXML_RESPONSE_SOURCEPATIENTINFO_SLOTNAME ,
				 * sourcePatientInfoValuesArray); olSlot.add(oSlot); bHaveData =
				 * true; }
				 */
				// Title
				// -------
				if (!NullChecker.isNullOrEmpty(doc.getTitle())) {

					final InternationalStringType oTitle = this
							.CreateSingleValueInternationalStringType(doc
									.getTitle());
					oExtObj.setName(oTitle);
					bHaveData = true;
				}

				// TODO: Type Code

				// ----------

				/*
				 * ClassificationType typeCodeClassification =
				 * createClassificationFromCodedData(doc.getTypeCode(),
				 * doc.getTypeCodeScheme(), doc.getTypeCodeDisplayName(),
				 * EBXML_RESPONSE_TYPECODE_CLASS_SCHEME, sDocumentUUID); if
				 * (typeCodeClassification != null) {
				 * olClassifications.add(typeCodeClassification); bHaveData =
				 * true; }
				 */

				// TODO: URI
				// ----
				/*
				 * if(!NullChecker.isNullOrEmpty(doc.getDocumentUri())) {
				 * SlotType1 oSlot = null; String documentUri =
				 * doc.getDocumentUri(); if (documentUri.length() <=
				 * EBXML_RESPONSE_URI_LINE_LENGTH) { oSlot =
				 * CreateSingleValueSlot(EBXML_RESPONSE_URI_SLOTNAME,
				 * documentUri); } else { int iStart = 0; String sURI =
				 * documentUri; int iTotalLen = sURI.length(); int iIndex = 1;
				 * String saURIPart[] = null; if ((iTotalLen %
				 * EBXML_RESPONSE_URI_LINE_LENGTH) == 0) { saURIPart = new
				 * String[iTotalLen / EBXML_RESPONSE_URI_LINE_LENGTH]; } else {
				 * saURIPart = new String[iTotalLen /
				 * EBXML_RESPONSE_URI_LINE_LENGTH + 1]; } while (iStart <
				 * iTotalLen) { if ((iStart + EBXML_RESPONSE_URI_LINE_LENGTH) >
				 * iTotalLen) { saURIPart[iIndex - 1] = iIndex + "|" +
				 * sURI.substring(iStart, iTotalLen); iStart = iTotalLen; } else
				 * { saURIPart[iIndex - 1] = iIndex + "|" +
				 * sURI.substring(iStart, iStart +
				 * EBXML_RESPONSE_URI_LINE_LENGTH); iStart +=
				 * EBXML_RESPONSE_URI_LINE_LENGTH; } iIndex++; } oSlot =
				 * CreateMultiValueSlot(EBXML_RESPONSE_URI_SLOTNAME, saURIPart);
				 * } // else if (oSlot != null) { olSlot.add(oSlot); bHaveData =
				 * true; } }
				 */

				if (bHaveData) {
					// Home community ID
					// ------------------
					oExtObj.setHome("urn:oid:" + homeCommunityId);
					// Repository Unique ID
					// ---------------------
					final SlotType1 oSlot = this
							.CreateSingleValueSlot(
									MockAdapterDocQueryPortImpl.EBXML_RESPONSE_REPOSITORY_UNIQUE_ID_SLOTNAME,
									MockAdapterDocQueryPortImpl.REPOSITORY_UNIQUE_ID);
					olSlot.add(oSlot);
					olRegObjs.add(oJAXBExtObj);
				}
			}
			// if we have any Object References, add them in now.
			// ---------------------------------------------------
			if (olObjRef.size() > 0) {
				olRegObjs.addAll(olObjRef);
			}
			// if we have any associations, add them in now.
			// ---------------------------------------------------
			if (olAssoc.size() > 0) {
				olRegObjs.addAll(olAssoc);
			}
		}
		return ret;
	}

	private AdhocQueryResponse createAdhocQueryResponseError(
			final AdhocQueryRequest adhocQueryRequest) {
		final oasis.names.tc.ebxml_regrep.xsd.query._3.ObjectFactory objFactory = new oasis.names.tc.ebxml_regrep.xsd.query._3.ObjectFactory();
		final AdhocQueryResponse ret = objFactory.createAdhocQueryResponse();
		ret.setRegistryErrorList(this.createDefaultRegistryErrorList());
		ret.setStatus(MockAdapterDocQueryPortImpl.XDS_QUERY_RESPONSE_STATUS_FAILURE);
		ret.setRequestId(adhocQueryRequest.getId());
		return ret;

	}

	/**
	 * This method creates a classification from a coded item.
	 * 
	 * @param oCoded
	 *            The coded to be transformed.
	 * @param sClassificationScheme
	 *            The classification scheme value.
	 * @param sDocumentId
	 *            The document ID for the document associated with this
	 *            classificaation.
	 * @return The classification created based on the information in the coded.
	 */

	private ClassificationType createClassificationFromCodedData(
			final String code, final String codeScheme,
			final String codeDisplayName, final String sClassificationScheme,
			final String sDocumentId) {
		MockAdapterDocQueryPortImpl.LOG
				.debug("DocumentRegistryHelper.CreateClassificationFromCodedData() -- Begin");
		final ClassificationType oClassification = new ClassificationType();
		oClassification.setId("urn:uuid:" + UUID.randomUUID().toString());
		boolean bHasCode = false;
		oClassification.setClassificationScheme(sClassificationScheme);
		oClassification.setClassifiedObject(sDocumentId);
		oClassification.setNodeRepresentation("");
		final List<SlotType1> olClassificationSlot = oClassification.getSlot();
		// Code
		// -----
		if (!NullChecker.isNullOrEmpty(code)) {
			oClassification.setNodeRepresentation(code);
			bHasCode = true;
		}
		// Code System
		// ------------
		if (!NullChecker.isNullOrEmpty(codeScheme)) {
			final SlotType1 oSlot = this
					.CreateSingleValueSlot(
							MockAdapterDocQueryPortImpl.EBXML_RESPONSE_CODE_CODESCHEME_SLOTNAME,
							codeScheme);
			olClassificationSlot.add(oSlot);
			bHasCode = true;
		}
		// DisplayName
		// ------------
		if (!NullChecker.isNullOrEmpty(codeDisplayName)) {
			final InternationalStringType oDisplayName = this
					.CreateSingleValueInternationalStringType(codeDisplayName);
			oClassification.setName(oDisplayName);
			bHasCode = true;
		}
		if (bHasCode) {
			return oClassification;
		} else {
			MockAdapterDocQueryPortImpl.LOG
					.debug("DocumentRegistryHelper.CreateClassificationFromCodedData() -- End");
			return null;
		}
	}

	private RegistryError createDefaultRegistryError() {
		final RegistryError ret = new RegistryError();
		ret.setErrorCode("XDSRegistryError");
		ret.setCodeContext("Internal Registry/Repository Error");
		ret.setSeverity("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error");
		return ret;
	}

	private RegistryErrorList createDefaultRegistryErrorList() {
		final RegistryErrorList ret = new RegistryErrorList();
		ret.getRegistryError().add(this.createDefaultRegistryError());
		ret.setHighestSeverity("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error");
		return ret;
	}

	/**
	 * This method creates a Slot containing a single value.
	 * 
	 * @param sSlotName
	 *            The name of the slot.
	 * @param saSlotValue
	 *            The array of values for the slot.
	 * @return The SlotType1 object containing the data passed in.
	 */
	private SlotType1 CreateMultiValueSlot(final String sSlotName,
			final String[] saSlotValue) {
		MockAdapterDocQueryPortImpl.LOG
				.debug("DocumentRegistryHelper.CreateMultiValueSlot() -- Begin");
		final SlotType1 oSlot = new SlotType1();
		oSlot.setName(sSlotName);
		final ValueListType oValueList = new ValueListType();
		oSlot.setValueList(oValueList);
		final List<String> olValue = oValueList.getValue();
		for (final String element : saSlotValue) {
			olValue.add(element);
		}
		MockAdapterDocQueryPortImpl.LOG
				.debug("DocumentRegistryHelper.CreateMultiValueSlot() -- End");
		return oSlot;
	}

	/**
	 * This method creates an InternationalStringType with a single value.
	 * 
	 * @param sLocStrValue
	 *            The value to be placed in the string.
	 * @return The InternationStringType that is being returned.
	 */

	private InternationalStringType CreateSingleValueInternationalStringType(
			final String sLocStrValue) {
		MockAdapterDocQueryPortImpl.LOG
				.debug("DocumentTransforms.CreateSingleValueInternationalStringType() -- Begin");
		final InternationalStringType oName = new InternationalStringType();
		final List<LocalizedStringType> olLocStr = oName.getLocalizedString();
		final LocalizedStringType oNameLocStr = new LocalizedStringType();
		olLocStr.add(oNameLocStr);
		oNameLocStr.setValue(sLocStrValue);
		MockAdapterDocQueryPortImpl.LOG
				.debug("DocumentTransforms.CreateSingleValueInternationalStringType() -- End");
		return oName;

	}

	/**
	 * This method creates a Slot containing a single value.
	 * 
	 * @param sSlotName
	 *            The name of the slot.
	 * @param sSlotValue
	 *            The value for the slot.
	 * @return The SlotType1 object containing the data passed in.
	 */

	private SlotType1 CreateSingleValueSlot(final String sSlotName,
			final String sSlotValue)

	{

		MockAdapterDocQueryPortImpl.LOG
				.debug("DocumentRegistryHelper.CreateSingleValueSlot() -- Begin");

		final String saSlotValue[] = new String[1];

		saSlotValue[0] = sSlotValue;

		MockAdapterDocQueryPortImpl.LOG
				.debug("DocumentRegistryHelper.CreateSingleValueSlot() -- End");

		return this.CreateMultiValueSlot(sSlotName, saSlotValue);

	}

	protected String formatCreationDate(final Date sourceDate)

	{

		return this.formatDate(sourceDate,
				MockAdapterDocQueryPortImpl.DATE_FORMAT_CREATION);

	}

	private String formatDate(final Date sourceDate, final String formatString)

	{

		String formatted = "";

		if ((sourceDate != null) && (formatString != null)) {

			try {

				final SimpleDateFormat formatter = new SimpleDateFormat(
						formatString);

				formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

				formatted = formatter.format(sourceDate);

			}

			catch (final Throwable t) {

				MockAdapterDocQueryPortImpl.LOG.warn(
						"Failed to format a date (" + sourceDate.toString()
								+ ") to a formatted string using the format '"
								+ formatString + "': " + t.getMessage(), t);

			}

		}

		return formatted;

	}

	protected String formatServiceDate(final Date sourceDate)

	{

		return this.formatDate(sourceDate,
				MockAdapterDocQueryPortImpl.DATE_FORMAT_SERVICE);

	}

	private Map<String, List<String>> getMapFromSlots(
			final List<SlotType1> slots) {
		final HashMap<String, List<String>> ret = new HashMap<String, List<String>>();
		for (final SlotType1 slot : slots) {
			if (!NullChecker.isNullOrEmpty(slot.getName())
					&& !NullChecker.isNullOrEmpty(slot.getValueList())
					&& !NullChecker.isNullOrEmpty(slot.getValueList()
							.getValue())) {
				List<String> values = ret.get(slot.getName());
				if (values == null) {
					values = new ArrayList<String>();
					ret.put(slot.getName(), values);
				}
				values.addAll(slot.getValueList().getValue());
			}
		}
		return ret;
	}

	@Override
	protected void initDefaultStrategies() {
	}

	@Override
	public Object marshalSendAndReceive(final Object requestPayload) {
		return this
				.respondingGatewayCrossGatewayQuery((RespondingGatewayCrossGatewayQueryRequest) requestPayload);
	}

	public void parseParamFormattedString(final String paramFormattedString,
			final List<String> resultCollection)

	{

		if ((paramFormattedString != null) && (resultCollection != null)) {

			if (paramFormattedString.startsWith("(")) {

				String working = paramFormattedString.substring(1);

				final int endIndex = working.indexOf(")");

				if (endIndex != -1) {

					working = working.substring(0, endIndex);

				}

				final String[] multiValueString = working.split(",");

				if (multiValueString != null) {

					for (final String element : multiValueString) {

						String singleValue = element;

						if (singleValue != null) {

							singleValue = singleValue.trim();

						}

						if (singleValue.startsWith("'")) {

							singleValue = singleValue.substring(1);

							final int endTickIndex = singleValue.indexOf("'");

							if (endTickIndex != -1) {

								singleValue = singleValue.substring(0,
										endTickIndex);

							}

						}

						resultCollection.add(singleValue);

					}

				}

			}

			else {

				resultCollection.add(paramFormattedString);

			}

		}

	}

	public List<String> parseParamFormattedStrings(
			final List<String> paramFormattedStrings)

	{

		final List<String> ret = new ArrayList<String>();

		for (final String paramFormattedString : paramFormattedStrings) {

			this.parseParamFormattedString(paramFormattedString, ret);

		}

		return ret;

	}

	public AdhocQueryResponse respondingGatewayCrossGatewayQuery(
			final RespondingGatewayCrossGatewayQueryRequest respondingGatewayCrossGatewayQueryRequest) {
		MockAdapterDocQueryPortImpl.LOG
				.info("respondingGatewayCrossGatewayQuery");
		AdhocQueryResponse ret;
		final AdhocQueryRequest body = respondingGatewayCrossGatewayQueryRequest
				.getAdhocQueryRequest();
		try {
			if (body != null) {
				final AdhocQueryType adhocQuery = body.getAdhocQuery();
				final List<SlotType1> slots = adhocQuery.getSlot();
				final Map<String, List<String>> queryParams = this
						.setDefaults(this.getMapFromSlots(slots));
				final List<Document> results = new ArrayList<Document>();
				if (!NullChecker
						.isNullOrEmpty(queryParams
								.get(MockAdapterDocQueryPortImpl.EBXML_DOCENTRY_PATIENT_ID))) {
					for (final String queryName : this
							.parseParamFormattedStrings(queryParams
									.get(MockAdapterDocQueryPortImpl.EBXML_DOCENTRY_CLASS_CODE))) {
						if (MockAdapterDocQueryPortImpl.LOG.isInfoEnabled()) {
							MockAdapterDocQueryPortImpl.LOG.info(queryName);
						}
						for (int i = 0; i < 2; i++) {
							final Document document = new Document();
							document.setBeginDate(new Date());
							document.setClassCode("TestClassCode" + i);
							document.setClassCodeDisplayName("TestClassCodeDisplayName"
									+ i);
							document.setClassCodeScheme("TestClassCodeScheme"
									+ i);
							document.setCreationTime(new Date());
							document.setDocumentId(i * 1000L);
							document.setEndDate(new Date());
							document.setTitle("TestTitle" + i);
							results.add(document);
						}

					}
					ret = this.createAdhocQueryResponse(body, results);
				} else {
					ret = this.createAdhocQueryResponseError(body);
				}
			} else {
				ret = this.createAdhocQueryResponseError(body);
			}
		} catch (final Throwable t) {
			MockAdapterDocQueryPortImpl.LOG.warn("Exception", t);
			ret = this.createAdhocQueryResponseError(body);
		} finally {
			MockAdapterDocQueryPortImpl.LOG
					.info("Exiting respondingGatewayCrossGatewayQuery");
		}
		return ret;
	}

	private String retrieveHomeCommunityId()

	{
		return "2.16.840.1.113883.4.349";
	}

	private Map<String, List<String>> setDefaults(
			final Map<String, List<String>> values) {
		if (NullChecker.isNullOrEmpty(values
				.get(MockAdapterDocQueryPortImpl.EBXML_DOCENTRY_CLASS_CODE))) {
			values.put(MockAdapterDocQueryPortImpl.EBXML_DOCENTRY_CLASS_CODE,
					Arrays.asList(new String[] { "34133-9" }));
		}
		if (NullChecker
				.isNullOrEmpty(values
						.get(MockAdapterDocQueryPortImpl.EBXML_DOCENTRY_CLASS_CODE_SCHEME))) {
			values.put(
					MockAdapterDocQueryPortImpl.EBXML_DOCENTRY_CLASS_CODE_SCHEME,
					Arrays.asList(new String[] { "2.16.840.1.113883.6.1" }));
		}
		final SimpleDateFormat formatter = new SimpleDateFormat(
				MockAdapterDocQueryPortImpl.DATE_FORMAT_FULL);
		if (NullChecker
				.isNullOrEmpty(values
						.get(MockAdapterDocQueryPortImpl.EBXML_DOCENTRY_SERVICE_START_TIME_FROM))) {
			values.put(
					MockAdapterDocQueryPortImpl.EBXML_DOCENTRY_SERVICE_START_TIME_FROM,
					Arrays.asList(new String[] { formatter.format(new Date(
							System.currentTimeMillis()
									- MockAdapterDocQueryPortImpl.TWO_YEARS)) }));
		}
		if (NullChecker
				.isNullOrEmpty(values
						.get(MockAdapterDocQueryPortImpl.EBXML_DOCENTRY_SERVICE_STOP_TIME_TO))) {
			values.put(
					MockAdapterDocQueryPortImpl.EBXML_DOCENTRY_SERVICE_STOP_TIME_TO,
					Arrays.asList(new String[] { formatter.format(new Date()) }));
		}
		final List<String> patientIds = values
				.get(MockAdapterDocQueryPortImpl.EBXML_DOCENTRY_PATIENT_ID);
		if (!NullChecker.isNullOrEmpty(patientIds)) {
			final List<String> fixed = new ArrayList<String>();
			final Pattern p = Pattern.compile("\\d{10}V\\d{6}");
			for (final String patientId : patientIds) {
				final Matcher m = p.matcher(patientId);
				if (m.find()) {
					fixed.add(m.group());
				}
			}
			values.put(MockAdapterDocQueryPortImpl.EBXML_DOCENTRY_PATIENT_ID,
					fixed);
		}
		return values;
	}

}
