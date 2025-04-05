package com.ndinhchien.langPractice.global.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JwtPayload {
    private final String type;
    private final String email;
    private final String role;
}
