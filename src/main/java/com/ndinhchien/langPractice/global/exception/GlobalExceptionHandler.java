package com.ndinhchien.langPractice.global.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.ndinhchien.langPractice.global.dto.BaseResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> handleBusinessException(BusinessException e) {
        log.error("BusinessException: {}", e.getMessage());
        return BaseResponse.error(e.getCode(), e.getMessage(), e.getData());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenException.class)
    public BaseResponse<?> handleTokenException(HttpServletRequest request, TokenException e) {
        log.error("TokenException: {}", e.getMessage());
        return BaseResponse.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> handleMethodArgumentNotValidException(HttpServletRequest request,
            MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        BindingResult result = e.getBindingResult();

        for (FieldError error : result.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }

        log.error("MethodArgumentNotValidException: {}", e.getMessage());
        return BaseResponse.error(HttpStatus.BAD_REQUEST, errorMap);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException", e);
        return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseResponse<?> handleException(Exception e) {
        log.error("Exception", e);
        return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
