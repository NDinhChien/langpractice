package com.ndinhchien.langPractice.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private static final HttpStatus INTENAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;

    private final int code;

    private Object data;

    public BusinessException(int code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public BusinessException() {
        this(INTENAL_SERVER_ERROR.value(), INTENAL_SERVER_ERROR.getReasonPhrase(), null);
    }

    public BusinessException(HttpStatus status) {
        this(status.value(), status.getReasonPhrase(), null);
    }

    public BusinessException(int code, String message) {
        this(code, message, null);
    }

    public BusinessException(HttpStatus status, String message) {
        this(status.value(), message, null);
    }

    public BusinessException(HttpStatus status, Object data) {
        this(status.value(), status.getReasonPhrase(), data);
    }

}
