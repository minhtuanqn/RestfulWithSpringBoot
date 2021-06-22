package com.customexception;

public class NoSuchEntityByIdException extends RuntimeException{
    public NoSuchEntityByIdException(String message) {
        super(message);
    }
}
