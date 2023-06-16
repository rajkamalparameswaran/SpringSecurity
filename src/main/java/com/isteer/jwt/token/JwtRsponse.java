package com.isteer.jwt.token;

public class JwtRsponse {
	
	private String jwt;

	public JwtRsponse(String jwt) {
		super();
		this.jwt = jwt;
	}

	public JwtRsponse() {
		super();
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	

}
