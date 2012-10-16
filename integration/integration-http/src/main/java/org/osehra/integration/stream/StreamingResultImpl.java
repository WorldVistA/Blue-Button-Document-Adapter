package org.osehra.integration.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

public class StreamingResultImpl implements StreamingOutput {

	/**
	 * @uml.property name="resource"
	 * @uml.associationEnd
	 */
	Resource resource;

	/**
	 * @param resource
	 * @uml.property name="resource"
	 */
	@Required
	public void setResource(final Resource resource) {
		this.resource = resource;
	}

	@Override
	public void write(final OutputStream output) throws IOException,
			WebApplicationException {
		int nRead;
		final byte[] data = new byte[16384];
		final InputStream in = this.resource.getInputStream();
		do {
			nRead = in.read(data, 0, data.length);
			output.write(data, 0, nRead);
		} while (nRead > 0);
		this.resource.getInputStream().close();
	}

}
