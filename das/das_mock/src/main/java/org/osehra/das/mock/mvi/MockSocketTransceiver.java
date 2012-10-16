package org.osehra.das.mock.mvi;

import org.osehra.integration.hl7.transceiver.Transceiver;
import org.osehra.integration.util.FileUtil;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class MockSocketTransceiver implements Transceiver<String> {

	/**
	 * @uml.property name="applicationContext"
	 * @uml.associationEnd readOnly="true"
	 */
	@Autowired
	ApplicationContext applicationContext;

	@Override
	public String transceive(final String payload) {
		try {
			if (payload.contains("QBP~Q22")
					&& payload.toLowerCase().contains("chdrone")) {
				final org.springframework.core.io.Resource resource = this.applicationContext
						.getResource("classpath:org/osehra/das/mock/endpoint/mvi/RSP_K22_SingleMatch.er7");
				final String rspK22ER7 = FileUtil.getResource(resource);
				return rspK22ER7;
			}
			if (payload.contains("QBP~Q22")
					&& payload.toLowerCase().contains("mpipatient")
					&& payload.contains("ONE")) {
				final org.springframework.core.io.Resource resource = this.applicationContext
						.getResource("classpath:org/osehra/das/mock/endpoint/mvi/RSP_K22_MultipleMatches.er7");
				final String rspK22ER7 = FileUtil.getResource(resource);
				return rspK22ER7;
			} else if (payload.contains("QBP~Q22")
					&& payload.toLowerCase().contains("chdrtwo")) {
				final org.springframework.core.io.Resource resource = this.applicationContext
						.getResource("classpath:org/osehra/das/mock/endpoint/mvi/RSP_K22_MultipleMatches.er7");
				final String rspK22ER7 = FileUtil.getResource(resource);
				return rspK22ER7;
			} else if (payload.contains("QBP~Q22")
					&& payload.toLowerCase().contains("error")) {
				final org.springframework.core.io.Resource resource = this.applicationContext
						.getResource("classpath:org/osehra/das/mock/endpoint/mvi/RSP_K22_Failure.er7");
				final String rspK22ER7 = FileUtil.getResource(resource);
				return rspK22ER7;
			} else {
				final org.springframework.core.io.Resource resource = this.applicationContext
						.getResource("classpath:org/osehra/das/mock/endpoint/mvi/RSP_K22_NoMatches.er7");
				final String rspK22ER7 = FileUtil.getResource(resource);
				return rspK22ER7;
			}
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}
