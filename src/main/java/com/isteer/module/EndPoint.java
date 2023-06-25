package com.isteer.module;

import java.util.List;

public class EndPoint {
	
	private int endPointId;
	private String endPointName;
	private List<String> authorities;
	public EndPoint(int endPointId, String endPointName, List<String> authorities) {
		super();
		this.endPointId = endPointId;
		this.endPointName = endPointName;
		this.authorities = authorities;
	}
	public EndPoint() {
		super();
	}
	public int getEndPointId() {
		return endPointId;
	}
	public void setEndPointId(int endPointId) {
		this.endPointId = endPointId;
	}
	public String getEndPointName() {
		return endPointName;
	}
	public void setEndPointName(String endPointName) {
		this.endPointName = endPointName;
	}
	public List<String> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

}
