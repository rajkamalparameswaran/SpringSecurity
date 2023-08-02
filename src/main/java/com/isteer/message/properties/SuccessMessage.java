package com.isteer.message.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("security.success.msg")
public class SuccessMessage {
	
	private String accountCreated;
	private String accountUpdated;
	private String accountDeleted;
	private String accountFetched;
	private String endPointAdded;
	private String loginSucess;
	public String getLoginSucess() {
		return loginSucess;
	}
	public void setLoginSucess(String loginSucess) {
		this.loginSucess = loginSucess;
	}
	public String getAccountCreated() {
		return accountCreated;
	}
	public void setAccountCreated(String accountCreated) {
		this.accountCreated = accountCreated;
	}
	public String getAccountUpdated() {
		return accountUpdated;
	}
	public void setAccountUpdated(String accountUpdated) {
		this.accountUpdated = accountUpdated;
	}
	public String getAccountDeleted() {
		return accountDeleted;
	}
	public void setAccountDeleted(String accountDeleted) {
		this.accountDeleted = accountDeleted;
	}
	public String getAccountFetched() {
		return accountFetched;
	}
	public void setAccountFetched(String accountFetched) {
		this.accountFetched = accountFetched;
	}
	public String getEndPointAdded() {
		return endPointAdded;
	}
	public void setEndPointAdded(String endPointAdded) {
		this.endPointAdded = endPointAdded;
	}
}
