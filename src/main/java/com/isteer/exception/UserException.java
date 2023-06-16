package com.isteer.exception;

import java.util.List;

public class UserException {
	
	private int statusCode;
	private String reason;
	private List<String> exception;
	public UserException(int statusCode, String reason, List<String> exception) {
		super();
		this.statusCode = statusCode;
		this.reason = reason;
		this.exception = exception;
	}
	public UserException() {
		super();
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
	public List<String> getException() {
		return exception;
	}
	public void setException(List<String> exception) {
		this.exception = exception;
	}
	
	

}
