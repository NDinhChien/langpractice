package com.ndinhchien.langPractice.domain.user.controller;

import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndinhchien.langPractice.domain.user.dto.AuthRequestDto.LoginRequestDto;
import com.ndinhchien.langPractice.domain.user.dto.AuthRequestDto.RegisterRequestDto;
import com.ndinhchien.langPractice.domain.user.dto.AuthResponseDto.JwtResponseDto;
import com.ndinhchien.langPractice.domain.user.entity.User;
import com.ndinhchien.langPractice.domain.user.service.AuthService;
import com.ndinhchien.langPractice.global.auth.UserDetailsImpl;
import com.ndinhchien.langPractice.global.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "auth", description = "Auth Related APIs")
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Check auth info")
    @GetMapping("/check")
    BaseResponse<User> check(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return BaseResponse.success("Auth info", userDetails.getUser());
    }

    @Operation(summary = "Register account")
    @PostMapping("/register")
    BaseResponse<User> register(
            @RequestBody @Valid RegisterRequestDto requestDto) {
        return BaseResponse.success("Register account", authService.register(requestDto));
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    BaseResponse<JwtResponseDto> login(
            @RequestBody @Valid LoginRequestDto requestDto,
            HttpServletResponse response) {
        return BaseResponse.success("Login success", authService.login(requestDto, response));
    }

    @Operation(summary = "Logout user")
    @DeleteMapping("/logout")
    BaseResponse<String> logout(
            @AuthenticationPrincipal @Nullable UserDetailsImpl userDetails,
            HttpServletResponse response) {
        authService.logout(userDetails != null ? userDetails.getUser() : null, response);
        return BaseResponse.success("Logged out.");
    }
}
