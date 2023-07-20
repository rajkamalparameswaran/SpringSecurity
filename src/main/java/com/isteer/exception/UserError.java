package com.isteer.exception;

import java.util.List;

public class UserError {

	private int statusCode;
	private String reason;
	private List<String> errorMsg;
	public UserError() {
		super();
	}
	public UserError(int statusCode, String reason, List<String> errorMsg) {
		super();
		this.statusCode = statusCode;
		this.reason = reason;
		this.errorMsg = errorMsg;
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
	public List<String> getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(List<String> errorMsg) {
		this.errorMsg = errorMsg;
	}
}
