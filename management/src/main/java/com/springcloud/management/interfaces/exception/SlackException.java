package com.springcloud.management.interfaces.exception;

public class SlackException extends RuntimeException {
    public SlackException(String message) {
        super(message);
    }
}
