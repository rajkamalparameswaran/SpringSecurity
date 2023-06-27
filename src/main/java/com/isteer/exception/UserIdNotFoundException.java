package com.isteer.exception;

import java.util.List;

public class UserIdNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private int statusCode;
	private String reason;
	private List<String> exception;

	public UserIdNotFoundException(int statusCode, String reason, List<String> exception) {
		super();
		this.statusCode = statusCode;
		this.reason = reason;
		this.exception = exception;
	}

	public UserIdNotFoundException() {
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
