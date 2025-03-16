package com.springcloud.hub.interfaces.exception;

public class CustomTimeoutException extends RuntimeException {
    public CustomTimeoutException(String message) {
        super(message);
    }
}
