package com.customexception;

public class NoSuchFieldSortByOfClassException extends RuntimeException{
    public NoSuchFieldSortByOfClassException(String message) {
        super(message);
    }
}
