package com.example.projectiku.exception;

public class CustomDuplicateResourceException extends RuntimeException{
    public CustomDuplicateResourceException(String message) {
        super(message);
    }
}
