package com.isteer.exception;

import java.util.List;

public class UserIdNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final int statusCode;
	private final String reason;
	private final List<String> errorMsg;

	public UserIdNotFoundException(int statusCode, String reason, List<String> exception) {
		super();
		this.statusCode = statusCode;
		this.reason = reason;
		this.errorMsg = exception;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getReason() {
		return reason;
	}

	public List<String> getException() {
		return errorMsg;
	}

}
