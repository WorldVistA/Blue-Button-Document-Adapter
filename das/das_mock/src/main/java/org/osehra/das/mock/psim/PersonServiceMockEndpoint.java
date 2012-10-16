/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.osehra.das.mock.psim;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * 
 * @author Barry Egbert.
 */
public class PersonServiceMockEndpoint extends WebServiceTemplate {

	@Override
	public Object marshalSendAndReceive(final Object requestPayload) {
		final List<String> vpids = new ArrayList<String>();
		vpids.add((String) requestPayload);
		return vpids;
	}
}
