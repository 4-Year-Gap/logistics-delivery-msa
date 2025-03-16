package com.springcloud.hub.interfaces.exception;

public class CustomConflictException extends RuntimeException {
    public CustomConflictException(String message) {
        super(message);
    }
}
