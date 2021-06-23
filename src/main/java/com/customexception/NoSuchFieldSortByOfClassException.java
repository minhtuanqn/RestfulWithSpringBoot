package com.customexception;

/**
 * Custom exception for no such field for sort by
 */
public class NoSuchFieldSortByOfClassException extends RuntimeException{
    public NoSuchFieldSortByOfClassException(String message) {
        super(message);
    }
}
