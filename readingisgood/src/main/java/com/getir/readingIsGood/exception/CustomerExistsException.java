package com.getir.readingIsGood.exception;

public class CustomerExistsException extends RuntimeException
{
	public CustomerExistsException(String message)
	{
		super(message);
	}
}
