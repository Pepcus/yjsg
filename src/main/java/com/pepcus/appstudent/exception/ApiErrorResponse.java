package com.pepcus.appstudent.exception;

import lombok.Data;

/**
 * Class used for error response when API throws error
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
@Data
public class ApiErrorResponse {

	private String error;
	private int status;
	private String url;
	private String message;

}
