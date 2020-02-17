package com.pepcus.appstudent.exception;

/**
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
public class BadRequestException extends RuntimeException {
	Integer errorCode;

	public BadRequestException(String message) {
		super(message);
	}
	
	public BadRequestException(String message, Integer errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

}
