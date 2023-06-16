package com.isteer.module;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


public class User {
	
	private int userId;
	private String userName;
	private String userFullName;
	private String userEmail;
	private String userPassword;
	private List<String> userAddresses;
	private List<String> userRoles;
	private String isAccountNonExpired;
	private String isAccountNonLocked;
	private String isCredentialsNonExpired;
	private String isEnabled;
	
	public User(int userId, String userName, String userFullName, String userEmail, String userPassword,
			List<String> userAddresses, List<String> userRoles, String isAccountNonExpired, String isAccountNonLocked,
			String isCredentialsNonExpired, String isEnabled) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userFullName = userFullName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.userAddresses = userAddresses;
		this.userRoles = userRoles;
		this.isAccountNonExpired = isAccountNonExpired;
		this.isAccountNonLocked = isAccountNonLocked;
		this.isCredentialsNonExpired = isCredentialsNonExpired;
		this.isEnabled = isEnabled;
	}
	public String getIsAccountNonExpired() {
		return isAccountNonExpired;
	}
	public void setIsAccountNonExpired(String isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}
	public String getIsAccountNonLocked() {
		return isAccountNonLocked;
	}
	public void setIsAccountNonLocked(String isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}
	public String getIsCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}
	public void setIsCredentialsNonExpired(String isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}
	public String getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
	public User() {
		super();
	}
	public User(int userId, String userName, String userFullName, String userEmail, String userPassword,
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
