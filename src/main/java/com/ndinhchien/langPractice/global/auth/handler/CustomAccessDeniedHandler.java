package com.ndinhchien.langPractice.global.auth.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.ndinhchien.langPractice.global.util.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@NoArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull AccessDeniedException accessDeniedException) throws IOException {

        ResponseUtil.fail(response, HttpStatus.FORBIDDEN, "Please login to use this function");
    }
}