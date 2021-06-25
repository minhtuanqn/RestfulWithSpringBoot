package com.customexception;

/**
 * Custom exception for duplicated unique identity
 */
public class DuplicatedEntityException extends SQLCustomException{

    public DuplicatedEntityException(String message) {
        super(message);
    }
}
