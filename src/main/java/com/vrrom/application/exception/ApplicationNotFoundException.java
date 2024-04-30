package com.vrrom.application.exception;

public class ApplicationNotFoundException extends ApplicationException {
    public ApplicationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
