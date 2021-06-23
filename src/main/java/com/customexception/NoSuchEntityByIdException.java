package com.customexception;

/**
 * Custom exception for no such entity by id
 */
public class NoSuchEntityByIdException extends RuntimeException{
    public NoSuchEntityByIdException(String message) {
        super(message);
    }
}
