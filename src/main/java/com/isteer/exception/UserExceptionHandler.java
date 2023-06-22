package com.isteer.exception;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.jsonwebtoken.JwtException;

@ControllerAdvice
public class UserExceptionHandler {

	@ExceptionHandler(value = { UserIdNotFoundException.class })
	public ResponseEntity<UserException> userIdNOtFoundException(UserIdNotFoundException idNotFoundException) {
		UserException userException = new UserException(idNotFoundException.getStatusCode(),
				idNotFoundException.getReason(), idNotFoundException.getException());

		return new ResponseEntity<UserException>(userException, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { SqlQueryException.class })
	public ResponseEntity<UserException> sqlQueryExcepton(SqlQueryException queryException) {
		UserException userException = new UserException(queryException.getStatusCode(), queryException.getReason(),
				queryException.getException());

		return new ResponseEntity<UserException>(userException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<UserException> handleCommonException(Exception exp) {
		List<String> exception = new ArrayList<>();
		exception.add(exp.getMessage());
		UserException userException = new UserException(0, "Cannot Do Process", exception);
		return new ResponseEntity<UserException>(userException, HttpStatus.BAD_REQUEST);

	}

}
