package com.pepcus.appstudent.exception;

/**
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
public class AuthorizationFailedException extends RuntimeException {
	
	public AuthorizationFailedException(String message) {
		super(message);
	}

}
