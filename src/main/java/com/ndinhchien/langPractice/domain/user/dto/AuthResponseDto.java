package com.ndinhchien.langPractice.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthResponseDto {

    @AllArgsConstructor
    @Getter
    public static class JwtResponseDto {
        private final String accessToken;
        private final String refreshToken;
    }
}
