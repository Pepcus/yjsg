package com.pepcus.appstudent.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Global exception handler for all APIs
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
@ControllerAdvice
@RestController
public class ApiExceptionHandler {

	/**
	 * Method used to handle {@link BadRequestException}
	 * 
	 * @param req
	 * @param ex
	 * @return
	 */
	@ExceptionHandler({ BadRequestException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiErrorResponse handleBadRequestException(HttpServletRequest req, BadRequestException ex) {
		ApiErrorResponse response = new ApiErrorResponse();
		response.setMessage(ex.getMessage());
		response.setError(HttpStatus.BAD_REQUEST.name());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setUrl(req.getRequestURI());
		response.setErrorCode(ex.errorCode);
		return response;
	}
	
	/**
	 * Method used to handle {@link AuthorizationFailedException}
	 * 
	 * @param req
	 * @param ex
	 * @return
	 */
	@ExceptionHandler({ AuthorizationFailedException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ApiErrorResponse handleAuthorizationFailedException(HttpServletRequest req, AuthorizationFailedException ex) {
		ApiErrorResponse response = new ApiErrorResponse();
		response.setMessage(ex.getMessage());
		response.setError(HttpStatus.UNAUTHORIZED.name());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setUrl(req.getRequestURI());
		return response;
	}

}
