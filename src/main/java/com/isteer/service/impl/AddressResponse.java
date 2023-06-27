package com.isteer.service.impl;

public class AddressResponse {

	private int statusCode;
	private String reason;
	private String addresses;

	public AddressResponse(int statusCode, String reason, String addresses) {
		super();
		this.statusCode = statusCode;
		this.reason = reason;
		this.addresses = addresses;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}
}
