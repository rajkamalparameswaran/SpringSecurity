package com.isteer.jwt.token;

public class JwtRsponse {

	private String jwt;

	private int statusCode;

	private String msg;

	public JwtRsponse() {
		super();
	}

	public JwtRsponse(String jwt, int statusCode, String msg) {
		super();
		this.jwt = jwt;
		this.statusCode = statusCode;
		this.msg = msg;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
}
