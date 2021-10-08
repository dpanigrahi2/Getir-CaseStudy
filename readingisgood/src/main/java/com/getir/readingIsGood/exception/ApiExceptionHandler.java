package com.getir.readingIsGood.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler
{

	@ExceptionHandler(value = NotValidException.class)
	public ResponseEntity<Object> handleNotValidException(NotValidException exception)
	{
		var payload =  new ApiExceptionModel(exception.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
		return new ResponseEntity<>(payload, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = NotFoundException.class)
	public ResponseEntity<Object> handleNotFoundException(NotFoundException exception)
	{
		var payload =  new ApiExceptionModel(exception.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
		return new ResponseEntity<>(payload, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = CustomerExistsException.class)
	public ResponseEntity<Object> handleCustomerExistsException(CustomerExistsException exception)
	{
		var payload =  new ApiExceptionModel(exception.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
		return new ResponseEntity<>(payload, HttpStatus.BAD_REQUEST);
	}
}
