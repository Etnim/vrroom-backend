package com.vrrom.vehicle.carInfoApi.exception;

import org.springframework.http.HttpStatusCode;

public class CarAPIException extends Exception {
    private final HttpStatusCode statusCode;

    public CarAPIException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    public CarAPIException( Throwable cause, HttpStatusCode status) {
        super( cause);
        this.statusCode = status;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

}
