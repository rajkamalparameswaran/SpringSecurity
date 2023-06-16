package com.isteer.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.isteer.jwt.token.JwtFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Bean
	public PasswordEncoder bCryptPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
	{
		httpSecurity.csrf((csr)->csr.disable()).
		authorizeHttpRequests((request)->request.requestMatchers("/authenticate","/addUser").permitAll()
				
				.requestMatchers("/updateUser","/deleteuserbyid/{userId}","/getuserbyid/{userId}","/getalluser").hasAuthority("ADMIN")
				.anyRequest().authenticated()).sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.httpBasic(Customizer.withDefaults());
		return httpSecurity.build();
		
		
	}
	@Bean
	public AuthenticationProvider authenticationProvider( PasswordEncoder passwordEncoder)
	{
		
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
		
		
	}
	
	@Bean
	public AuthenticationManager authenticationManager( AuthenticationConfiguration configuration) throws Exception
	{
		return configuration.getAuthenticationManager();
	}

}
