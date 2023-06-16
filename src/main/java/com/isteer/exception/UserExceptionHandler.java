package com.isteer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
	public ResponseEntity<UserException> sqlQueryExcepton(UserTokenException tokenException)
	{
		UserException userException=new UserException(tokenException.getStatusCode(), tokenException.getReason(), tokenException.getException());
		
		return new ResponseEntity<UserException>(userException,HttpStatus.FORBIDDEN);
	}
	
	
	
	

}
