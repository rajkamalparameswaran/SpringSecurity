package com.isteer.jwt.token;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isteer.message.properties.FailedMessage;
import com.isteer.statuscode.StatusCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomBearerTokenExceptionEntryPoint implements AuthenticationEntryPoint {
	
	private static final Logger AUDITLOG=LogManager.getLogger("AuditLogs");

	@Autowired 
	private FailedMessage property;
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		Map<String, Object> body = new HashMap<>();
		body.put("StatusCode", StatusCode.USERAUTHENTICATIONFAILED.getCode());
		body.put("Reason", authException.getMessage());
		body.put("errorMsg", property.getAuthenticationFailed());
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
		AUDITLOG.info(property.getAuthenticationFailed());
	}
}
