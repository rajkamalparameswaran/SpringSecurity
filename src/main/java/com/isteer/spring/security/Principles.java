package com.isteer.spring.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.isteer.module.User;

public class Principles implements UserDetails {
	
	private User user;

	public Principles(User user) {
		super();
		this.user = user;
	}

	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<String> roles=user.getUserRoles();
		List<SimpleGrantedAuthority> list=new ArrayList<>();
		for(String role:roles)
		{
			list.add(new SimpleGrantedAuthority(role));
		}
		return list;
	}

	@Override
	public String getPassword() {
		
		return user.getUserPassword();
	}

	@Override
	public String getUsername() {
		
		return user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		String isAccountNonExpired="true";
		
		return isAccountNonExpired.equalsIgnoreCase(user.getIsAccountNonExpired());
		
	}

	@Override
	public boolean isAccountNonLocked() {
       String isAccountNonLocked="true";

		
		return isAccountNonLocked.equalsIgnoreCase(user.getIsAccountNonLocked());
		
	}

	@Override
	public boolean isCredentialsNonExpired() {
		String isCredentialsNonExpired="true";
		return isCredentialsNonExpired.equalsIgnoreCase(user.getIsCredentialsNonExpired());
		
	}

	@Override
	public boolean isEnabled() {
		String isEnabled="true";
		
		return isEnabled.equalsIgnoreCase(user.getIsEnabled());
		
	}

}
