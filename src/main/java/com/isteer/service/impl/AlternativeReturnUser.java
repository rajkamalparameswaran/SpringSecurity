package com.isteer.service.impl;

import com.isteer.module.User;

public class AlternativeReturnUser {

	private int statusCode;
	private String result;
	private User user;

	public AlternativeReturnUser(int statusCode, String result, User user) {
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
