package com.isteer.service.impl;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressesResponse {

	private int statusCode;
	private String reason;
	private List<String> addresses;
	public AddressesResponse(int statusCode, String reason, List<String> addresses) {
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
	public List<String> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}
	
	
	
}
