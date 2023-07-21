package com.isteer.message.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("security.error.msg")
public class FailedMessage {
	
	private String notValidData;
	private String emptyAddress;
	private String emptyRoles;
	private String nameLengthInvalid;
	private String accountCreationFailed;
	private String userIdNotFound;
	private String userUpdationFailed;
	private String emptyPrivilege;
	private String accountDeletProcessFailed;
	private String dataFetchProcssFailed;
	private String endPointCannotAdded;
	private String wrongUserIdOrPassword;
	private String loginFailed;
	private String processFailed;
	private String accessDenied;
	private String authenticationFailed;
	private String invalidName;
	private String nameOrEmailAlreadyExist;
	private String invalidSqlQuery;
	private String emailLengthInvalid;
	private String noDataFound;
	public String getNoDataFound() {
		return noDataFound;
	}
	public void setNoDataFound(String noDataFound) {
		this.noDataFound = noDataFound;
	}
	public String getEmailLengthInvalid() {
		return emailLengthInvalid;
	}
	public void setEmailLengthInvalid(String emailLengthInvalid) {
		this.emailLengthInvalid = emailLengthInvalid;
	}
	public String getInvalidSqlQuery() {
		return invalidSqlQuery;
	}
	public void setInvalidSqlQuery(String invalidSqlQuery) {
		this.invalidSqlQuery = invalidSqlQuery;
	}
	public String getNameOrEmailAlreadyExist() {
		return nameOrEmailAlreadyExist;
	}
	public void setNameOrEmailAlreadyExist(String nameOrEmailAlreadyExist) {
		this.nameOrEmailAlreadyExist = nameOrEmailAlreadyExist;
	}
	public String getNotValidData() {
		return notValidData;
	}
	public void setNotValidData(String notValidData) {
		this.notValidData = notValidData;
	}
	public String getEmptyAddress() {
		return emptyAddress;
	}
	public void setEmptyAddress(String emptyAddress) {
		this.emptyAddress = emptyAddress;
	}
	public String getEmptyRoles() {
		return emptyRoles;
	}
	public void setEmptyRoles(String emptyRoles) {
		this.emptyRoles = emptyRoles;
	}
	public String getNameLengthInvalid() {
		return nameLengthInvalid;
	}
	public void setNameLengthInvalid(String nameLengthInvalid) {
		this.nameLengthInvalid = nameLengthInvalid;
	}
	public String getAccountCreationFailed() {
		return accountCreationFailed;
	}
	public void setAccountCreationFailed(String accountCreationFailed) {
		this.accountCreationFailed = accountCreationFailed;
	}
	public String getUserIdNotFound() {
		return userIdNotFound;
	}
	public void setUserIdNotFound(String userIdNotFound) {
		this.userIdNotFound = userIdNotFound;
	}
	public String getUserUpdationFailed() {
		return userUpdationFailed;
	}
	public void setUserUpdationFailed(String userUpdationFailed) {
		this.userUpdationFailed = userUpdationFailed;
	}
	public String getEmptyPrivilege() {
		return emptyPrivilege;
	}
	public void setEmptyPrivilege(String emptyPrivilege) {
		this.emptyPrivilege = emptyPrivilege;
	}
	public String getAccountDeletProcessFailed() {
		return accountDeletProcessFailed;
	}
	public void setAccountDeletProcessFailed(String accountDeletProcessFailed) {
		this.accountDeletProcessFailed = accountDeletProcessFailed;
	}
	public String getDataFetchProcssFailed() {
		return dataFetchProcssFailed;
	}
	public void setDataFetchProcssFailed(String dataFetchProcssFailed) {
		this.dataFetchProcssFailed = dataFetchProcssFailed;
	}
	public String getEndPointCannotAdded() {
		return endPointCannotAdded;
	}
	public void setEndPointCannotAdded(String endPointCannotAdded) {
		this.endPointCannotAdded = endPointCannotAdded;
	}
	public String getWrongUserIdOrPassword() {
		return wrongUserIdOrPassword;
	}
	public void setWrongUserIdOrPassword(String wrongUserIdOrPassword) {
		this.wrongUserIdOrPassword = wrongUserIdOrPassword;
	}
	public String getLoginFailed() {
		return loginFailed;
	}
	public void setLoginFailed(String loginFailed) {
		this.loginFailed = loginFailed;
	}
	public String getProcessFailed() {
		return processFailed;
	}
	public void setProcessFailed(String processFailed) {
		this.processFailed = processFailed;
	}
	public String getAccessDenied() {
		return accessDenied;
	}
	public void setAccessDenied(String accessDenied) {
		this.accessDenied = accessDenied;
	}
	public String getAuthenticationFailed() {
		return authenticationFailed;
	}
	public void setAuthenticationFailed(String authenticationFailed) {
		this.authenticationFailed = authenticationFailed;
	}
	public String getInvalidName() {
		return invalidName;
	}
	public void setInvalidName(String invalidName) {
		this.invalidName = invalidName;
	}

	

	

}
