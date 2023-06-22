package com.isteer.jwt.token;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AccessDeniedEntryPoint implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
	
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(response.SC_FORBIDDEN);

		
		Map<String,Object> body=new HashMap<>();
		body.put("StatusCode", 0);
		body.put("Reason", accessDeniedException.getMessage());
		body.put("Exception", "You dont have to access this page");
		
		final ObjectMapper mapper=new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//	private final HandlerExceptionResolver exceptionResolver;
//
//	public AccessDeniedEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
//		super();
//		this.exceptionResolver = exceptionResolver;
//	}
//
//	@Override
//	public void handle(HttpServletRequest request, HttpServletResponse response,
//			AccessDeniedException accessDeniedException) throws IOException, ServletException {
//		
//
//		exceptionResolver.resolveException(request, response, null, accessDeniedException);
//
//	}

}
