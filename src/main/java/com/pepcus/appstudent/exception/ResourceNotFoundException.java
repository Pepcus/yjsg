package com.pepcus.appstudent.exception;

/**
 * 
 * @author Sandeep Vishwakarma
 * @since 05-03-2020
 *
 */
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	Integer errorCode;

	public ResourceNotFoundException() {
		super("No Data found for request.");
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
