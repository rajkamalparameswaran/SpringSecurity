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
		List<String> roles = user.getUserRoles();
		List<String> privileges = user.getPrivileges();
		List<GrantedAuthority> list = new ArrayList<>();

		for (String privilege : privileges) {

			list.add(new SimpleGrantedAuthority(privilege));
		}
		for (String role : roles) {

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

		return user.isAccountNonExpired();

	}

	@Override
	public boolean isAccountNonLocked() {

		return user.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return user.isCredentialsNonExpired();

	}

	@Override
	public boolean isEnabled() {

		return user.isEnabled();

	}

}
