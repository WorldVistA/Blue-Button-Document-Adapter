package org.osehra.das.mock.vista;

import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.FileUtil;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class MockVistAExtendedViewEndpoint implements
		ServiceInvoker<Object, String> {

	/**
	 * @uml.property name="applicationContext"
	 * @uml.associationEnd readOnly="true"
	 */
	@Autowired
	ApplicationContext applicationContext;

	@Override
	public String invoke(final Object obj) throws ServiceInvocationException {
		try {
			Assert.assertNotEmpty(obj, "ICN is required!");
			// CHDRONE
			if ("1012581676V377802".contains((String) obj)) {
				final org.springframework.core.io.Resource resource = this.applicationContext
						.getResource("classpath:org/osehra/das/mock/endpoint/vista/mvi/MPI RETURN CORRELATION DATA.txt");
				final String extendedViewData = FileUtil.getResource(resource);
				return extendedViewData;
			} else if ("1111111111V111111".equals(obj)) {
				final org.springframework.core.io.Resource resource = this.applicationContext
						.getResource("classpath:org/osehra/das/mock/endpoint/vista/mvi/MPI RETURN CORRELATION DATA_MPIPATIENT_ONE.txt");
				final String extendedViewData = FileUtil.getResource(resource);
				return extendedViewData;
			} else if ("2222222222V222222".equals(obj)) {
				final org.springframework.core.io.Resource resource = this.applicationContext
						.getResource("classpath:org/osehra/das/mock/endpoint/vista/mvi/MPI RETURN CORRELATION DATA_MPIPATIENT_ONE_2.txt");
				final String extendedViewData = FileUtil.getResource(resource);
				return extendedViewData;
			} else if ("3333333333V333333".equals(obj)) {
				final org.springframework.core.io.Resource resource = this.applicationContext
						.getResource("classpath:org/osehra/das/mock/endpoint/vista/mvi/MPI RETURN CORRELATION DATA_MPIPATIENT_ONE.txt");
				final String extendedViewData = FileUtil.getResource(resource);
				return extendedViewData;
			} else if ("4444444444V444444".equals(obj)) {
				final org.springframework.core.io.Resource resource = this.applicationContext
						.getResource("classpath:org/osehra/das/mock/endpoint/vista/mvi/MPI RETURN CORRELATION DATA_MPIPATIENT_ONE.txt");
				final String extendedViewData = FileUtil.getResource(resource);
				return extendedViewData;
			}
			return null;
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
