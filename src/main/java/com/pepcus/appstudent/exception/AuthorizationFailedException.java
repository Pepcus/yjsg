package com.pepcus.appstudent.exception;

/**
 * Custom exception class to handle exception those are related with authorization failure
 * @author Shubham Solanki
 * @since 12-02-2018
 */
public class AuthorizationFailedException extends RuntimeException {
	
    /**
     * AuthorizationFailedException constructor
     * @param message
     */
	public AuthorizationFailedException(String message) {
		super(message);
	}

}
