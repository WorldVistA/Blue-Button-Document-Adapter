/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.osehra.das.wrapper.nwhin.doc;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequest;
import org.osehra.integration.util.NullChecker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * The endpoint to call the adpater's webservice.
 *
 * @author Asha Amritraj
 */
public class AdapterDocQueryEndpoint {
	private static final String EBXML_DOCENTRY_CLASS_CODE = "$XDSDocumentEntryClassCode";
	private static final String EBXML_DOCENTRY_CLASS_CODE_SCHEME = "$XDSDocumentEntryClassCodeScheme";
	private static final String EBXML_DOCENTRY_PATIENT_ID = "$XDSDocumentEntryPatientId";
	private static final String EBXML_DOCENTRY_SERVICE_START_TIME_FROM = "$XDSDocumentEntryServiceStartTimeFrom";
	private static final String EBXML_DOCENTRY_SERVICE_STOP_TIME_TO = "$XDSDocumentEntryServiceStopTimeTo";
	private static final String EBXML_DOCENTRY_STATUS = "$XDSDocumentEntryStatus";

	// We need to be able to do a search using AdhocQueryRequest parameters, but
	// XDS.b does not have a search parameter slot name defined for repository
	// ID and document ID. So we had to create our own custom ones.
	// ----------------------------------------------------------------------------

	private static final String EBXML_RESPONSE_DOCID_NAME = "XDSDocumentEntry.uniqueId";
	private static final String EBXML_RESPONSE_REPOSITORY_UNIQUE_ID_SLOTNAME = "repositoryUniqueId";
	private static final String EBXML_RESPONSE_SIZE_SLOTNAME = "size";

	private WebServiceTemplate adapterDocQueryServiceTemplate;

	private String homeCommunityOid;
	private String homeCommunityOidExt;

	private String extractDocumentName(final ExtrinsicObjectType eot) {
		String ret = null;

		if (!NullChecker.isNullOrEmpty(eot.getName())
				&& !NullChecker.isNullOrEmpty(eot.getName()
						.getLocalizedString())
				&& !NullChecker.isNullOrEmpty(eot.getName()
						.getLocalizedString().get(0))) {
			ret = eot.getName().getLocalizedString().get(0).getValue();
		}

		return ret;
	}

	private String extractExternalIdentifier(
			final List<ExternalIdentifierType> externalIds, final String name) {
		String ret = null;

		for (final ExternalIdentifierType eit : externalIds) {
			if (!NullChecker.isNullOrEmpty(eit.getName())
					&& !NullChecker.isNullOrEmpty(eit.getName()
							.getLocalizedString())
					&& !NullChecker.isNullOrEmpty(eit.getName()
							.getLocalizedString().get(0))
					&& name.equalsIgnoreCase(eit.getName().getLocalizedString()
							.get(0).getValue())) {
				ret = eit.getValue();
				break;
			}
		}

		return ret;
	}

	/**
	 * Extract the Repository ID from the slots
	 *
	 * @param slots
	 *            The slots to be searched.
	 * @return The Repository Id.
	 */
	private String extractRepositoryId(final List<SlotType1> slots) {
		String repositoryId = null;
		final List<String> slotValues = this
				.extractSlotValues(
						slots,
						AdapterDocQueryEndpoint.EBXML_RESPONSE_REPOSITORY_UNIQUE_ID_SLOTNAME);
		if ((slotValues != null) && (!slotValues.isEmpty())) {
			// We should only have one - so use the first one.
			// -------------------------------------------------
			repositoryId = slotValues.get(0).trim();
		}
		return repositoryId;
	}

	private String extractSize(final List<SlotType1> slots) {
		String ret = null;
		final List<String> slotValues = this.extractSlotValues(slots,
				AdapterDocQueryEndpoint.EBXML_RESPONSE_SIZE_SLOTNAME);
		if (!NullChecker.isNullOrEmpty(slotValues)) {
			ret = slotValues.get(0).trim();
		}
		return ret;
	}

	private List<String> extractSlotValues(final List<SlotType1> slots,
			final String slotName) {
		List<String> returnValues = null;
		for (final SlotType1 slot : slots) {
			if ((slot.getName() != null) && (slot.getName().length() > 0)
					&& (slot.getValueList() != null)
					&& (slot.getValueList().getValue() != null)
					&& (slot.getValueList().getValue().size() > 0)) {

				if (slot.getName().equals(slotName)) {
					final ValueListType valueListType = slot.getValueList();
					final List<String> slotValues = valueListType.getValue();
					returnValues = new ArrayList<String>();
					for (final String slotValue : slotValues) {
						returnValues.add(slotValue);
					}
				}
			}

		}
		return returnValues;
	}

	public List<Map<String, String>> getDocuments(final String patientId,
			final Date fromDate, final Date toDate) {
		// Constrcut the query
		final List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
		final RespondingGatewayCrossGatewayQueryRequest request = new RespondingGatewayCrossGatewayQueryRequest();
		final String classCodeValues = "34133-9";
		final String classCodeScheme = "2.16.840.1.113883.6.1";
		final oasis.names.tc.ebxml_regrep.xsd.query._3.ObjectFactory queryObjFact = new oasis.names.tc.ebxml_regrep.xsd.query._3.ObjectFactory();
		final AdhocQueryRequest queryRequest = queryObjFact
				.createAdhocQueryRequest();
		request.setAdhocQueryRequest(queryRequest);
		// Set the options
		final ResponseOptionType rot = new ResponseOptionType();
		rot.setReturnType("LeafClass");
		rot.setReturnComposedObjects(true);
		queryRequest.setResponseOption(rot);
		// Create the AdhocQuery
		final oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjFact = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
		final AdhocQueryType query = rimObjFact.createAdhocQueryType();
		queryRequest.setAdhocQuery(query);

		query.setHome(this.homeCommunityOidExt);
		query.setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");

		final SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyyMMddhhmmss");

		final List<SlotType1> slots = query.getSlot();
		slots.add(this.loadSlot(patientId + "^^^&" + this.homeCommunityOid
				+ "&ISO", AdapterDocQueryEndpoint.EBXML_DOCENTRY_PATIENT_ID));
		slots.add(this.loadSlot(classCodeValues,
				AdapterDocQueryEndpoint.EBXML_DOCENTRY_CLASS_CODE));
		slots.add(this.loadSlot(classCodeScheme,
				AdapterDocQueryEndpoint.EBXML_DOCENTRY_CLASS_CODE_SCHEME));
		slots.add(this
				.loadSlot(
						"('urn:ihe:iti:2010:StatusType:DeferredCreation', 'urn:oasis:names:tc:ebxmlregrep:StatusType:Approved')",
						AdapterDocQueryEndpoint.EBXML_DOCENTRY_STATUS));
		if (fromDate != null) {
			final String serviceStartTimeFrom = formatter.format(fromDate);
			slots.add(this
					.loadSlot(
							serviceStartTimeFrom,
							AdapterDocQueryEndpoint.EBXML_DOCENTRY_SERVICE_START_TIME_FROM));
		}
		if (toDate != null) {
			final String serviceStopTimeTo = formatter.format(toDate);
			slots.add(this
					.loadSlot(
							serviceStopTimeTo,
							AdapterDocQueryEndpoint.EBXML_DOCENTRY_SERVICE_STOP_TIME_TO));
		}
		// Call the adapter's query webserivce
		final AdhocQueryResponse response = (AdhocQueryResponse) this.adapterDocQueryServiceTemplate
				.marshalSendAndReceive(request);

		final RegistryObjectListType registryObjectListType = response
				.getRegistryObjectList();
		if (registryObjectListType != null) {
			final List<JAXBElement<? extends IdentifiableType>> identifiable = registryObjectListType
					.getIdentifiable();
			for (final JAXBElement<? extends IdentifiableType> i : identifiable) {
				if (i.getValue() instanceof ExtrinsicObjectType) {
					final ExtrinsicObjectType eot = (ExtrinsicObjectType) i
							.getValue();
					final String homeCommunityId = eot.getHome();
					final String repositoryId = this.extractRepositoryId(eot
							.getSlot());
					final String documentUniqueId = this
							.extractExternalIdentifier(
									eot.getExternalIdentifier(),
									AdapterDocQueryEndpoint.EBXML_RESPONSE_DOCID_NAME);
					final String documentName = this.extractDocumentName(eot);
					final String size = this.extractSize(eot.getSlot());

					// Put the fields into the response
					final HashMap<String, String> doc = new HashMap<String, String>();
					doc.put("homeCommunityId", homeCommunityId);
					doc.put("documentRepositoryId", repositoryId);
					doc.put("documentUniqueId", documentUniqueId);
					doc.put("documentName", documentName);
					doc.put("size", size);
					doc.put("source", homeCommunityId);
					ret.add(doc);
				}
			}
		}
		// Send the return response
		return ret;
	}

	/**
	 * Create slots.
	 */
	private SlotType1 loadSlot(final String value, final String slotName) {
		final ValueListType valueListType = new ValueListType();
		valueListType.getValue().add(value);
		final SlotType1 slot = new SlotType1();
		slot.setValueList(valueListType);
		slot.setName(slotName);

		return slot;
	}

	@Required
	public void setAdapterDocQueryServiceTemplate(
			final WebServiceTemplate adapterDocQueryServiceTemplate) {
		this.adapterDocQueryServiceTemplate = adapterDocQueryServiceTemplate;
	}

	@Required
	public void setHomeCommunityOid(final String homeCommunityOid) {
		this.homeCommunityOid = homeCommunityOid;
	}

	@Required
	public void setHomeCommunityOidExt(final String homeCommunityOidExt) {
		this.homeCommunityOidExt = homeCommunityOidExt;
	}
}
