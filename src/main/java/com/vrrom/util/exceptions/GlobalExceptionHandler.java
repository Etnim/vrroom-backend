package com.vrrom.util.exceptions;

import com.vrrom.application.exception.ApplicationException;
import com.vrrom.vehicle.carInfoApi.exception.CarAPIException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Objects;
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
        return new ResponseEntity<>(ex.getMessage(), status);
    }

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
    public ResponseEntity<List<String>> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> messages = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            errors.append(error.getDefaultMessage()).append(". ");
        }
        return ResponseEntity.badRequest().body(errors.toString());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getCause().getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleConversionError(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() == java.time.LocalDate.class) {
            return handleLocalDateTypeMismatch(ex);
        }
        if (Objects.requireNonNull(ex.getRequiredType()).isEnum()) {
            return handleEnumTypeMismatch(ex);
        }
        if (ex.getCause() instanceof ConversionFailedException) {
            ConversionFailedException cfe = (ConversionFailedException) ex.getCause();
            String expectedType = cfe.getTargetType().getType().getTypeName();
            String actualType = cfe.getValue() == null ? "null" : cfe.getValue().getClass().getTypeName();
            String detailedMessage = String.format("Failed to convert value '%s' from type '%s' to required type '%s'; %s",
                    ex.getValue(), actualType, expectedType, cfe.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(detailedMessage);
        }
        String genericMessage = String.format("Failed to convert parameter: %s. Expected type '%s'.",
                ex.getName(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericMessage);
    }

    private ResponseEntity<String> handleLocalDateTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(String.format("The date value '%s' is not valid. Expected format: yyyy-MM-dd.",
                        ex.getValue()));
    }

    private ResponseEntity<String> handleEnumTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(String.format("Invalid value '%s' for type '%s'.",
                        ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Object> handleRestClientException(RestClientException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service unavailable or request timed out.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + ex.getMessage());
    }
}