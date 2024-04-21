package com.vrrom.util.exceptions;

import com.vrrom.vehicle.carInfoApi.exception.CarAPIException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vrrom.application.exception.ApplicationException;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CarAPIException.class)
    public ResponseEntity<String> handleApiException(CarAPIException ex) {
        HttpStatusCode status = ex.getStatusCode();
        if (ex.getStatusCode() == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(ex.getMessage(), status);}

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String cause = ex.getRootCause() != null ? ex.getRootCause().getMessage() : "";
        return new ResponseEntity<>("Unable to complete request due to data integrity issues.  " + cause, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<String> handleMailException(MailException ex) {
        String cause = ex.getCause() != null ? ex.getCause().getMessage() : "";
        return new ResponseEntity<>("Email sending failed " + cause, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<String> handleApplicationException(ApplicationException ex) {
        String cause = ex.getCause() != null ? ex.getCause().getMessage() : "";
        return new ResponseEntity<>("Application processing error " + cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        String details = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>("Constraint violation error " + details, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getCause().getMessage());
    }



    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Object> handleRestClientException(RestClientException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service unavailable or request timed out.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + ex.getMessage());
    }
}