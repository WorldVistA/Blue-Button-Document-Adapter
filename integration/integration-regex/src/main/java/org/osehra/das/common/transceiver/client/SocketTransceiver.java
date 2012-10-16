package org.osehra.das.common.transceiver.client;

import org.osehra.das.common.validation.Assert;
import org.osehra.das.common.validation.NullChecker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * SocketTransceiver.
 * @author  Asha Amritraj
 */
public abstract class SocketTransceiver implements Transceiver<String> {
	private static final int DEFAULT_BUFFER = 512;
	private static final int DEFAULT_TIMEOUT = 10000;
	private static final Log LOG = LogFactory.getLog(SocketTransceiver.class);
	/**
	 * @uml.property  name="defaultNetworkTimeOut"
	 */
	private final int defaultNetworkTimeOut = SocketTransceiver.DEFAULT_TIMEOUT;
	/**
	 * @uml.property  name="endTime"
	 */
	private long endTime;
	/**
	 * @uml.property  name="host"
	 */
	private String host;
	/**
	 * @uml.property  name="port"
	 */
	private int port;
	/**
	 * @uml.property  name="timeout"
	 */
	private int timeout;

	protected SocketTransceiver() {
	}

	protected final Socket connect() {
		final long startTime = System.currentTimeMillis();
		this.setEndTime(startTime + this.timeout);
		final Socket socket = new Socket();
		try {
			final InetSocketAddress isoc = new InetSocketAddress(this.host,
					this.port);
			socket.setSoTimeout(this.timeout);
			socket.connect(isoc, this.defaultNetworkTimeOut);
		} catch (final UnknownHostException e) {
			final String msg = "Unable to contact remote host at address "
					+ this.host + " and port " + this.port;
			throw new RuntimeException(msg, e);
		} catch (final IOException e) { // also SocketTimeoutException
			final String msg = "Unable to contact remote host at address "
					+ this.host + " and port " + this.port;
			throw new RuntimeException(msg, e);
		}
		return socket;
	}

	protected final void disconnect(final Socket socket) {
		if (NullChecker.isEmpty(socket)) {
			return;
		}
		try {
			if (socket.isConnected() && !socket.isClosed()) {
				final InputStream is = socket.getInputStream();
				final OutputStream os = socket.getOutputStream();

				if (NullChecker.isNotEmpty(is)) {
					is.close();
				}
				if (NullChecker.isNotEmpty(os)) {
					os.close();
				}
				if (NullChecker.isNotEmpty(socket)) {
					socket.close();
				}
			}
		} catch (final IOException e) {
			// Do Nothing.
			SocketTransceiver.LOG.warn("Exception in disconnect", e);
		}
	}

	protected abstract String formatIncomingMessage(String payload);

	protected abstract String formatOutgoingMessage(String payload);

	/**
	 * @uml.property  name="endMarker"
	 */
	protected abstract String getEndMarker();

	protected abstract int getEndMarkerPosition();

	protected final InputStream getInputStream(final Socket socket) {
		Assert.assertNotEmpty(socket, "Socket cannot be null!");
		Assert.assertFalse(socket.isClosed(), "Trancseiver is not connected.");
		try {
			return socket.getInputStream();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected final OutputStream getOutputStream(final Socket socket) {
		Assert.assertNotEmpty(socket, "Socket cannot be null!");
		Assert.assertFalse(socket.isClosed(), "Trancseiver is not connected.");
		try {
			return socket.getOutputStream();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected final String receive(final Socket socket, final String endMarker) {
		Assert.assertNotEmpty(socket, "Socket cannot be null!");
		Assert.assertFalse(socket.isClosed(), "Trancseiver is not connected.");

		final StringBuffer buffer = new StringBuffer();
		// final InputStreamReader reader = new InputStreamReader(this
		// .getInputStream(socket));
		int i = 0;
		final byte[] arr = new byte[SocketTransceiver.DEFAULT_BUFFER];
		try {
			while ((i != -1)
					&& (buffer.indexOf(endMarker) < this.getEndMarkerPosition())) {
				i = this.getInputStream(socket).read(arr, 0, arr.length);
				if (i > 0) {
					for (int j = 0; j < i; j++) {
						buffer.append((char) arr[j]);
					}
				}
				Assert.assertFalse(System.currentTimeMillis() > this.endTime,
						"Receive operation timed out.");
			}
		} catch (final IOException ioe) {
			throw new RuntimeException("Receive operation failed.", ioe);
		} // Do not close reader since socket is reused.
		return buffer.toString();
	}

	protected final void send(final Socket socket, final String data) {
		Assert.assertNotEmpty(socket, "Socket cannot be null!");
		Assert.assertFalse(socket.isClosed(), "Trancseiver is not connected.");
		try {
			this.getOutputStream(socket).write(data.getBytes());
			this.getOutputStream(socket).flush();
		} catch (final IOException e) {
			throw new RuntimeException("Send operation failed.");
		}
		if (System.currentTimeMillis() > this.endTime) {
			throw new RuntimeException("Send operation timed out.");
		}
	}

	/**
	 * @param value
	 * @uml.property  name="endTime"
	 */
	protected final void setEndTime(final long value) {
		this.endTime = value;
	}

	/**
	 * @param theHost
	 * @uml.property  name="host"
	 */
	@Required
	public final void setHost(final String theHost) {
		this.host = theHost;
	}

	/**
	 * @param thePort
	 * @uml.property  name="port"
	 */
	@Required
	public final void setPort(final int thePort) {
		this.port = thePort;
	}

	/**
	 * @param theTimeout
	 * @uml.property  name="timeout"
	 */
	@Required
	public final void setTimeout(final int theTimeout) {
		this.timeout = theTimeout;
	}

	@Override
	public final String transceive(final String payload) {
		Socket socket = null;
		try {
			// Connect
			socket = this.connect();
			Assert.assertNotEmpty(socket, "Socket cannot be null!");
			// Send - format outgoing
			this.send(socket, this.formatOutgoingMessage(payload));
			// Receive and format incoming
			return this.formatIncomingMessage(this.receive(socket,
					this.getEndMarker()));
		} finally {
			this.disconnect(socket);
			socket = null;
		}
	}
}
