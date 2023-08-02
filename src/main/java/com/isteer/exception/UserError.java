package com.isteer.exception;

import java.util.List;

public class UserError {

	private int statusCode;
	private String errorMsg;
	private List<String> reasons;
	public UserError() {
		super();
	}
	public UserError(int statusCode, String reason, List<String> errorMsg) {
		super();
		this.statusCode = statusCode;
		this.errorMsg = reason;
		this.reasons = errorMsg;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String reason) {
		this.errorMsg = reason;
	}
	public List<String> getReasons() {
		return reasons;
	}
	public void setReasons(List<String> errorMsg) {
		this.reasons = errorMsg;
	}
}
