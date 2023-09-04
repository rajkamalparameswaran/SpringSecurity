package com.isteer.spring.security;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class PrincipleConfiguration {

	@Bean
	public Principal getPrincipal() {
		return new CustomPrincipal();
	}

	private static class CustomPrincipal implements Principal {

		@Override
		public String getName() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				return authentication.getName();
			}
			return null;
		}

		public Collection<? extends GrantedAuthority> getAuthorities() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				return authentication.getAuthorities();
			}
			return Collections.emptyList();
		}

		
	}

}
