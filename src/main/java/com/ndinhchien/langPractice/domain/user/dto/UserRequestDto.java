package com.ndinhchien.langPractice.domain.user.dto;

import static com.ndinhchien.langPractice.domain.user.dto.AuthRequestDto.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

public class UserRequestDto {
    @Getter
    public static class UpdateProfileDto {
        @Schema(example = "Nguyen Dinh Chien")
        @Pattern(regexp = FULLNAME_PATTERN, message = FULLNAME_MESSAGE)
        private String fullName;

        @Schema(example = "I am a Backend Developer")
        private String bio;

        @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_MESSAGE)
        private String userName;
    }
}
