package com.isteer.service.impl;

import com.isteer.module.User;

public class UserResponse {
	
	private int statusCode;
	private String result;
	private ReturnUser user;
	public UserResponse(int statusCode, String result, ReturnUser user) {
		super();
		this.statusCode = statusCode;
		this.result = result;
		this.user = user;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public ReturnUser getUser() {
		return user;
	}
	public void setUser(ReturnUser user) {
		this.user = user;
	}

}
