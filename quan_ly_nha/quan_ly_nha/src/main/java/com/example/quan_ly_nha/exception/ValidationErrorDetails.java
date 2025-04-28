package com.example.quan_ly_nha.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrorDetails extends GlobalExceptionHandler.ErrorDetails {
    private Map<String, String> errors;

    public ValidationErrorDetails(LocalDateTime timestamp, String message, String details, String errorCode, Map<String, String> errors) {
        super(timestamp, message, details, errorCode);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}