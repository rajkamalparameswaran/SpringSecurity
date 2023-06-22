package com.isteer.exception;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class UserExceptionHandler {
	
	@ExceptionHandler(value = {UserIdNotFoundException.class})
	public ResponseEntity<UserException> userIdNOtFoundException(UserIdNotFoundException idNotFoundException)
	{
		UserException userException=new UserException(idNotFoundException.getStatusCode(), idNotFoundException.getReason(), idNotFoundException.getException());
		
		return new ResponseEntity<UserException>(userException,HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(value = {SqlQueryException.class})
	public ResponseEntity<UserException> sqlQueryExcepton(SqlQueryException queryException)
	{
		UserException userException=new UserException(queryException.getStatusCode(), queryException.getReason(), queryException.getException());
		
		return new ResponseEntity<UserException>(userException,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = {UserTokenException.class})
	public ResponseEntity<UserException> userTokenException(UserTokenException exception)
	{
		List<String> list=new ArrayList<>();
		list.add(exception.getMessage());
		UserException userException=new UserException(0, "Cannot do process", list);
		
		return new ResponseEntity<UserException>(userException,HttpStatus.UNAUTHORIZED);
	}
	
	
	
	

}
