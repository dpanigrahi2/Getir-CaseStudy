package com.getir.readingIsGood.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiExceptionModel
{
	private final String message;
	private final HttpStatus httpStatus;
	private final LocalDateTime timestamp;
}
