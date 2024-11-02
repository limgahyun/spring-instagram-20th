package com.ceos20.spring_boot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ExceptionCode(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }
}