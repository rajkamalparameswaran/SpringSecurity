package com.isteer.statuscode;

public enum StatusCode {
	
	SUCESSCODE(1),
	ACCOUNTCREATEDFAILED(0),
	USERIDNOTFOUND(-1),
	ACCOUNTUPDATEDFAILED(-2),
	ACCOUNTFETCHINGFAILED(-3),
	ENDPOINTADDEDFAILED(-4),
	ENDPOINTACESSUPDATEDFAILED(-5),
	ACCOUNTDELETIONFAILED(-6),
	USERIDORPASSWORDINCORRECT(-7),
	USERAUTHENTICATIONFAILED(-10),
	USERACESSDENIED(-11),
	SQLEXCEPTIONCODE(-20);
	
	 int code;

	private StatusCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	
	
	
	
	
	
	
	

}
