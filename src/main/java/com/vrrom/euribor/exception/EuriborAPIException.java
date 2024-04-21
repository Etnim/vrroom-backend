package com.vrrom.euribor.exception;

import org.springframework.http.HttpStatusCode;

public class EuriborAPIException extends Exception {
    private final HttpStatusCode statusCode;

    public EuriborAPIException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
