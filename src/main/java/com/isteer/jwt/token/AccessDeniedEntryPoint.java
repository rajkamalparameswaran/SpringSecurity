package com.isteer.jwt.token;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isteer.logs.Log4j2;
import com.isteer.message.properties.FailedMessage;
import com.isteer.statuscode.StatusCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AccessDeniedEntryPoint implements AccessDeniedHandler {

	private Logger logger=LogManager.getLogger(AccessDeniedEntryPoint.class);
	
	@Autowired 
	FailedMessage property;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		Map<String, Object> body = new HashMap<>();
		body.put("StatusCode", StatusCode.USERACESSDENIED.getCode());
		body.put("Reason", accessDeniedException.getMessage());
		body.put("errorMsg",property.getAccessDenied());
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
		logger.error(property.getAccessDenied());
		Log4j2.getAuditlog().info(property.getAccessDenied());
	}
}
