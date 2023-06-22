package com.isteer.jwt.token;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomBearerTokenExceptionEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(response.SC_UNAUTHORIZED);
		
		Map<String,Object> body=new HashMap<>();
		body.put("StatusCode", 0);
		body.put("Reason", authException.getMessage());
		body.put("Exception", "Invalid Token");
		
		final ObjectMapper mapper=new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
		
	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//	private final HandlerExceptionResolver exceptionResolver;
//
//	public CustomBearerTokenExceptionEntryPoint(
//			@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
//		super();
//		this.exceptionResolver = exceptionResolver;
//	}
//
//	@Override
//	public void commence(HttpServletRequest request, HttpServletResponse response,
//			AuthenticationException authException) throws IOException, ServletException {
//
//		exceptionResolver.resolveException(request, response, null, authException);
//
//	}

}
