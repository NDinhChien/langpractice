package com.ndinhchien.langPractice.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

public class AuthRequestDto {

    public static final String PASSWORD_MESSAGE = "Password must be between 8 and 20 characters long and contain at least one lowercase letter, one uppercase letter, one number, and one special character (@$!%*?&).";
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";

    public static final String USERNAME_MESSAGE = "Usernames must be between 3 and 15 characters long and contain only letters, numbers, and underscores.";
    public static final String USERNAME_PATTERN = "^[_\\w]{3,15}$";

    public static final String FULLNAME_MESSAGE = "Full name must be between 2 and 50 characters long and contain only alphabetic characters and spaces.";
    public static final String FULLNAME_PATTERN = "^[a-zA-Z\\s]{2,50}$";

    @Getter
    public static class RegisterRequestDto {
        @Schema(example = "test@email.com")
        @Email
        @NotBlank
        private String email;

        @Schema(example = "test")
        @Pattern(regexp = USERNAME_PATTERN, message = USERNAME_MESSAGE)
        @NotBlank
        private String userName;

        @Schema(example = "Aa123456&")
        @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_MESSAGE)
        @NotBlank
        private String password;

        @Schema(example = "Aa123456&")
        @NotBlank
        private String confirmPassword;
    }

    @Getter
    public static class LoginRequestDto {
        @Schema(example = "test@email.com")
        @Email
        @NotBlank
        private String email;

        @Schema(example = "Aa123456&")
        @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_MESSAGE)
        @NotBlank
        private String password;
    }
}
