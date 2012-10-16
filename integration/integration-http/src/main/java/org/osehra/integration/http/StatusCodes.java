package org.osehra.integration.http;

/**
 *
 * This class is an enum of status codes
 * @author parumalla
 *
 *
 */
public enum StatusCodes {

	SC_200(200,"OK"),
	SC_404(404,"Not Found"),
	SC_206(206,"Partial List"),
	SC_500(500,"Internal Server Error");

	 private int statusCode;
	 private String description;

	private StatusCodes(int statusCode,String description){

		this.statusCode=statusCode;
		this.description=description;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
