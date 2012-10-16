package org.osehra.das.common.transceiver.hl7;

/**
 * The HL7MLLP Transceiver for communication with external systems over a
 * socket.
 * 
 * @author Asha Amritraj
 */
public class HL7MLLPTransceiver extends
		org.osehra.das.common.transceiver.client.SocketTransceiver {

	public static final String FSCR = "\u001C\r";
	public static final String VT = "\u000B";

	@Override
	protected String formatIncomingMessage(final String payload) {
		String ret;
		if (payload.startsWith(HL7MLLPTransceiver.VT)
				&& payload.endsWith(HL7MLLPTransceiver.FSCR)) {
			ret = payload.substring(1, payload.length() - 2);
		} else {
			ret = payload;
		}
		return ret;
	}

	@Override
	protected String formatOutgoingMessage(final String payload) {
		return HL7MLLPTransceiver.VT + payload + HL7MLLPTransceiver.FSCR;
	}

	@Override
	protected String getEndMarker() {
		return HL7MLLPTransceiver.FSCR;
	}

	@Override
	protected int getEndMarkerPosition() {
		return 0;
	}

}
