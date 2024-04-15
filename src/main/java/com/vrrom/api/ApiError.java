package com.vrrom.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ApiError {
    private HttpStatusCode status;
    private String message;
    private String details;

    public ApiError(HttpStatusCode status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
// Getters and setters omitted for brevity
}