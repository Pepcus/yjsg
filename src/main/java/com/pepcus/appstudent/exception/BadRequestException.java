package com.pepcus.appstudent.exception;

/**
 * Custom exception class to handle exception those are related with bad request
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

}
