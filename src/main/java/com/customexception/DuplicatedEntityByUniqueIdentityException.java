package com.customexception;

/**
 * Custom exception for duplicated unique identity
 */
public class DuplicatedEntityByUniqueIdentityException extends RuntimeException{

    public DuplicatedEntityByUniqueIdentityException(String message) {
        super(message);
    }
}
