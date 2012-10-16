package org.osehra.das.wrapper.nwhin.doc;

import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Facade for the adapter doc query/retrieve.
 *
 * @author Asha Amritraj
 */
public class AdapterDocQueryRetrieveFacade {

	AdapterDocQueryEndpoint adapterDocQueryEndpoint;

	AdapterDocRetrieveEndpoint adapterDocRetrieveEndpoint;


	public String getDocument(final String patientId, final Date startDate,
			final Date endDate, final String userName) {
		Assert.assertNotEmpty(patientId, "Patient id cannot be null!");

		final List<Map<String, String>> documents = this.getDocuments(
				patientId, startDate, endDate, userName);

		if (NullChecker.isNotEmpty(documents)) {
			final Map<String, String> docMap = documents.get(0);
			final String document = this.getDocument(patientId,
					docMap.get("documentUniqueId"),
					docMap.get("documentRepositoryId"),
					docMap.get("homeCommunityId"), userName);
			return document;
		}
		return null;
	}

	public String getDocument(final String icn, final String documentUniqueId,
			final String documentRepositoryId, final String homeCommunityId,
			final String userName) {
		Assert.assertNotEmpty(userName, "User cannot be null!");

		return this.adapterDocRetrieveEndpoint.getDocument(icn,
				documentUniqueId, documentRepositoryId, homeCommunityId,
				userName);
	}

	public List<Map<String, String>> getDocuments(final String patientId,
			final Date fromDate, final Date toDate, final String userName) {
		Assert.assertNotEmpty(patientId, "Patient id cannot be null!");

		return this.adapterDocQueryEndpoint.getDocuments(patientId, fromDate,
				toDate);
	}

	public void setAdapterDocQueryEndpoint(
			AdapterDocQueryEndpoint adapterDocQueryEndpoint) {
		this.adapterDocQueryEndpoint = adapterDocQueryEndpoint;
	}

	public void setAdapterDocRetrieveEndpoint(
			AdapterDocRetrieveEndpoint adapterDocRetrieveEndpoint) {
		this.adapterDocRetrieveEndpoint = adapterDocRetrieveEndpoint;
	}
}
