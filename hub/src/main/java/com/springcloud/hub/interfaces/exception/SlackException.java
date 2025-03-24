package com.springcloud.hub.interfaces.exception;

public class SlackException extends RuntimeException {
    public SlackException(String message) {
        super(message);
    }
}
