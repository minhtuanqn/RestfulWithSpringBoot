package com.customexception;

public class NoSuchFieldForSortException extends RuntimeException{
    NoSuchFieldForSortException(String message) {
        super(message);
    }
}
