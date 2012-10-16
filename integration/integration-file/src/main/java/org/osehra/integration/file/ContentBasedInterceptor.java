package org.osehra.integration.file;

import org.osehra.integration.core.interceptor.Interceptor;
import org.osehra.integration.core.interceptor.InterceptorException;
import org.osehra.integration.util.NullChecker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

public class ContentBasedInterceptor implements Interceptor<Object, Object> {

	Map<String, Interceptor<Object, Object>> intereceptors;

	@Override
	public Object intercept(final Object object) throws InterceptorException {
		if (NullChecker.isEmpty(object)) {
			return null;
		}
		try {
			String documentType = null;
			if (InputStream.class.isInstance(object)) {
				documentType = URLConnection
						.guessContentTypeFromStream((InputStream) object);
			} else if (byte[].class.isInstance(object)) {
				final ByteArrayInputStream bs = new ByteArrayInputStream(
						(byte[]) object);
				documentType = URLConnection.guessContentTypeFromStream(bs);
			} else if (String.class.isInstance(object)) {
				final ByteArrayInputStream bs = new ByteArrayInputStream(
						((String) object).getBytes());
				documentType = URLConnection.guessContentTypeFromStream(bs);
			}

			if (this.intereceptors.containsKey(documentType)) {
				return this.intereceptors.get(documentType).intercept(object);
			}
		} catch (final IOException ex) {
			throw new InterceptorException(ex);
		}
		return object;
	}

	@Required
	public void setIntereceptors(
			final Map<String, Interceptor<Object, Object>> intereceptors) {
		this.intereceptors = intereceptors;
	}

}
