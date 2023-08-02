package com.isteer.exception;

import java.util.List;

public class UserIdNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final int statusCode;
	private final String errorMsg;
	private final List<String> reasons;

	public UserIdNotFoundException(int statusCode, String reason, List<String> exception) {
		super();
		this.statusCode = statusCode;
		this.errorMsg = reason;
		this.reasons = exception;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public List<String> getException() {
		return reasons;
	}

}
