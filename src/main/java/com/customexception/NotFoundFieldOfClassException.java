package com.customexception;

/**
 * Custom exception for no such field for sort by
 */
public class NotFoundFieldOfClassException extends ClassCustomException{

    public NotFoundFieldOfClassException(String message) {
        super(message);
    }
}
