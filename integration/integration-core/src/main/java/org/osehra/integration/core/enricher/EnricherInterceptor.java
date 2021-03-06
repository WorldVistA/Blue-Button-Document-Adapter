package org.osehra.integration.core.enricher;

import org.osehra.integration.core.interceptor.Interceptor;
import org.osehra.integration.core.interceptor.InterceptorException;

import org.springframework.beans.factory.annotation.Required;

/**
 * Interceptor to enrich documents.
 *
 * @author Julian Jewel
 */
public class EnricherInterceptor implements Interceptor<Object, Object> {

	private Enricher<Object> enricher;

	public Enricher<Object> getEnricher() {
		return enricher;
	}

	@Override
	public Object intercept(final Object object) throws InterceptorException {
		try {
			return this.enricher.enrich(object);
		} catch (final EnricherException ex) {
			throw new InterceptorException(ex);
		}
	}

	@Required
	public void setEnricher(final Enricher<Object> enricher) {
		this.enricher = enricher;
	}



}
