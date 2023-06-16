package com.isteer.service.impl;

import java.util.List;

public class ReturnUser {

	private int userId;
	private String userName;
	private String userFullName;
	private String userEmail;
	private String userPassword;
	private List<String> userAddresses;
	private List<String> userRoles;

	public ReturnUser(int userId, String userName, String userFullName, String userEmail, String userPassword,
			List<String> userAddresses, List<String> userRoles) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userFullName = userFullName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userAddresses = userAddresses;
		this.userRoles = userRoles;
	}

	public ReturnUser() {
		super();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public List<String> getUserAddresses() {
		return userAddresses;
	}

	public void setUserAddresses(List<String> userAddresses) {
		this.userAddresses = userAddresses;
	}

	public List<String> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<String> userRoles) {
		this.userRoles = userRoles;
	}

}
