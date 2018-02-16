package com.pepcus.appstudent.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
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
public class ExceptionHandler {

	/**
	 * Method used to handle {@link BadRequestException}
	 * 
	 * @param req
	 * @param ex
	 * @return
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler({ BadRequestException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiErrorResponse handleBadRequestException(HttpServletRequest req, BadRequestException ex) {
		ApiErrorResponse response = new ApiErrorResponse();
		response.setMessage(ex.getMessage());
		response.setError(HttpStatus.BAD_REQUEST.name());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setUrl(req.getRequestURI());
		return response;
	}

}
