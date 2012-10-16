package org.osehra.das.mock.th.mpi;

import org.osehra.das.mock.mvi.MockSocketTransceiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Mock MPI Socket Transceiver
 * 
 * @author Asha Amritraj
 */
public class MockSocketServer implements Runnable {

	/** ASCII code for carriage return. */
	private static final byte CR = 0x0D;

	/** End of block byte definition. */
	private static final byte EOB = 0x1C;

	private static final Log log = LogFactory.getLog(MockSocketServer.class);

	private static final byte SOB = 0x0B;

	public static void main(final String[] args) {
		try {
			final ServerSocket serverSocket = new ServerSocket(5001);
			Socket clientSocket = null;
			while (true) {
				try {
					clientSocket = serverSocket.accept();
					final MockSocketServer clientSocketServer = new MockSocketServer();
					clientSocketServer.setClientSocket(clientSocket);
					final Thread newThread = new Thread(clientSocketServer);
					newThread.start();
					MockSocketServer.log.error("MockSocketServer started...");
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
			}

		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * @uml.property name="reader"
	 */
	private BufferedReader reader;
	/**
	 * @uml.property name="socket"
	 */
	private Socket socket;
	/**
	 * @uml.property name="stop"
	 */
	private boolean stop = false;

	/**
	 * @uml.property name="writer"
	 */
	private BufferedWriter writer;

	public MockSocketServer() {
	}

	public String formatOutgoing(final String msg) {
		final StringBuffer buf = new StringBuffer();
		buf.append((char) MockSocketServer.SOB);
		buf.append(msg);
		buf.append((char) MockSocketServer.CR);
		buf.append((char) MockSocketServer.EOB);
		buf.append((char) MockSocketServer.CR);
		buf.append((char) MockSocketServer.CR);

		return buf.toString();
	}

	public String getNextMessage(final BufferedReader reader) {
		try {
			final char[] buffer = new char[2048];
			final int len = reader.read(buffer, 0, buffer.length);
			if (len > 0) {
				return new String(buffer, 0, len);
			} else {
				return null;
			}
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public boolean quit() {
		return false;
	}

	@Override
	public void run() {
		try {

			if ((this.reader == null) && (this.socket != null)) {
				this.reader = new BufferedReader(new InputStreamReader(
						this.socket.getInputStream()));
				this.writer = new BufferedWriter(new OutputStreamWriter(
						this.socket.getOutputStream()));
			}
		} catch (final IOException ex) {
			MockSocketServer.log.error("MockSocketServer failed "
					+ Thread.currentThread().getName() + " " + ex);
			MockSocketServer.log.error("Exiting "
					+ Thread.currentThread().getName());
			return;
		}

		while (!this.stop) {
			try {
				final String request = this.getNextMessage(this.reader);
				if (request == null) {
					try {
						Thread.sleep(5000);
					} catch (final InterruptedException ex) {
					}
					continue;
				}

				String response = (String) this.transceive(request);
				if (response != null) {
					response = this.formatOutgoing(response);
					this.writer.write(response);
					this.writer.flush();
				}

			} catch (final IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		try {
			if (null != this.reader) {
				this.reader.close();
			}
			if (null != this.writer) {
				this.writer.close();
			}
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}
		MockSocketServer.log
				.info("Exiting " + Thread.currentThread().getName());
	}

	public void setClientSocket(final Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void setStop() {
		this.stop = true;
	}

	public Object transceive(final Object msg) {
		final MockSocketTransceiver transceiver = new MockSocketTransceiver();
		return transceiver.transceive((String) msg);
	}

}
