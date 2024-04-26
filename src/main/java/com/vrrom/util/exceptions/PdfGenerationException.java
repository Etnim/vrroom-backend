package com.vrrom.util.exceptions;

import org.springframework.http.HttpStatus;

public class PdfGenerationException extends Exception {
    public PdfGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}