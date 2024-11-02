package com.ceos20.spring_boot.config.exception;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {
    private final HttpStatus status;
    private final String message;

    public ExceptionResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static ExceptionResponse from(ExceptionCode exceptionCode) {
        return new ExceptionResponse(exceptionCode.getStatus(), exceptionCode.getMessage());
    }
}