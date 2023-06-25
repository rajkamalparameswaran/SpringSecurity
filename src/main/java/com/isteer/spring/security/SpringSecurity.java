package com.isteer.spring.security;

import java.util.ArrayList;
import java.util.List;

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

import com.isteer.dao.impl.UserDaoImpl;
import com.isteer.dao.layer.UserDao;
import com.isteer.jwt.token.AccessDeniedEntryPoint;
import com.isteer.jwt.token.CustomBearerTokenExceptionEntryPoint;
import com.isteer.jwt.token.JwtFilter;
import com.isteer.module.EndPoint;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

	@Autowired
	UserDetailsService userDetailsService;
	
	

	@Autowired
	JwtFilter jwtFilter;

	@Autowired
	CustomBearerTokenExceptionEntryPoint customBearerTokenExceptionEntryPoint;

	@Autowired
	AccessDeniedEntryPoint accessDeniedEntryPoint;
	
	@Autowired
	UserDao dao;

	@Bean
	public PasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
	
		
		httpSecurity.cors((cors) -> cors.disable());
		httpSecurity.csrf((csr) -> csr.disable());
		
		
		List<EndPoint> endPoints=dao.getAllEndPointDetails();
		for(EndPoint endPoint:endPoints)
		{
			if(endPoint.getAuthorities().contains("PermitAll"))
			{
				System.out.println(endPoint.getEndPointName()+"          "+endPoint.getAuthorities());
				httpSecurity.authorizeHttpRequests((request)->request.requestMatchers(endPoint.getEndPointName()).permitAll());
				
			}else
			{
				System.out.println(endPoint.getEndPointName()+"          "+endPoint.getAuthorities());
			String[] authority = endPoint.getAuthorities().stream().toArray(String[] ::new);
		
			httpSecurity.authorizeHttpRequests((request)->request.requestMatchers(endPoint.getEndPointName()).hasAnyAuthority(authority));
			}
		}
		
		httpSecurity.exceptionHandling((exp) -> exp.authenticationEntryPoint(customBearerTokenExceptionEntryPoint)
				.accessDeniedHandler(accessDeniedEntryPoint))
		.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
         httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
         
         return httpSecurity.build();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		httpSecurity.authorizeHttpRequests((request) -> request
//				.requestMatchers("/authenticate", "/addUser","/addnewendpoint","/updateendpointbyendpointid","/getAllEndPointDetails").permitAll().requestMatchers("/grandPermissions")
//				.hasAuthority("ADMIN"));
//		
//		httpSecurity.authorizeHttpRequests((request) -> request.
//				requestMatchers("/getuserbyid/{userId}").hasAnyAuthority("canGetOne", "ADMIN"));
//		
//		httpSecurity.authorizeHttpRequests((request) -> request.
//				requestMatchers("/getalluser").hasAnyAuthority("canGetAll", "ADMIN"));
//		
//		httpSecurity.exceptionHandling((exp) -> exp.authenticationEntryPoint(customBearerTokenExceptionEntryPoint)
//				.accessDeniedHandler(accessDeniedEntryPoint))
//		.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//		
//         httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
				
		
		
		
				
				
		
				
				
				
				
				
				
				
				
				
				
//				.requestMatchers("/authenticate", "/addUser").permitAll().requestMatchers("/grandPermissions")
//				.hasAuthority("ADMIN").requestMatchers("/updateUser").hasAnyAuthority("canUpdate", "ADMIN")
//				.requestMatchers("/deleteuserbyid/{userId}").hasAnyAuthority("canDelete", "ADMIN")
//				.requestMatchers("/getalluser").hasAnyAuthority("canGetAll", "ADMIN")
//				.requestMatchers("/getuserbyid/{userId}").hasAnyAuthority("canGetOne", "ADMIN")
//				.requestMatchers("/getaddressbyuserId/{userId}", "/getaddressbyuserIdandaddressId/{userId}/{addressId}")
//				.hasAnyAuthority("ADMIN", "canGetAddress").anyRequest().authenticated())
//				.exceptionHandling((exp) -> exp.authenticationEntryPoint(customBearerTokenExceptionEntryPoint)
//						.accessDeniedHandler(accessDeniedEntryPoint))
//				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//		httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	

		

	}

	@Bean
	public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {

		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;

	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
