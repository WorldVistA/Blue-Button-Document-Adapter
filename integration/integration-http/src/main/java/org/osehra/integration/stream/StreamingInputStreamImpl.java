package org.osehra.integration.stream;

import org.osehra.integration.util.NullChecker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

public class StreamingInputStreamImpl implements StreamingOutput {

	InputStream in;

	public StreamingInputStreamImpl(InputStream in) {
		this.in = in;
	}

	@Override
	public void write(final OutputStream output) throws IOException,
			WebApplicationException {
		if(NullChecker.isNotEmpty(in)) {
			int nRead;
			final byte[] data = new byte[4096];
			do {
				nRead = in.read(data, 0, data.length);
				output.write(data, 0, nRead);
			} while (nRead > 0);
			this.in.close();
		}
	}
}
