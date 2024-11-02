package com.ceos20.spring_boot.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.webjars.NotFoundException;
import org.apache.coyote.BadRequestException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error("MethodArgumentNotValidException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(BAD_REQUEST).body("잘못된 요청입니다.");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(final NotFoundException e) {
        logger.error("NotFoundException 발생: {}", e.getMessage(), e);
        return ResponseEntity.status(NOT_FOUND).body(new ExceptionResponse(NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(final BadRequestException e) {
        logger.error("BadRequestException 발생: {}", e.getMessage(), e);
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse(BAD_REQUEST, e.getMessage()));
    }
}