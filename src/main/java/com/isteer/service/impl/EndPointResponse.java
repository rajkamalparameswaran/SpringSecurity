package com.isteer.service.impl;

import com.isteer.module.EndPoint;

public class EndPointResponse {

	private int statusCode;
	private String message;
	private EndPoint endPoint;

	public EndPointResponse() {
		super();
	}

	public EndPointResponse(int statusCode, String message, EndPoint endPoint) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.endPoint = endPoint;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public EndPoint getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(EndPoint endPoint) {
		this.endPoint = endPoint;
	}
}
