package com.ndinhchien.langPractice.domain.pack.dto;

import com.ndinhchien.langPractice.domain.pack.entity.type.LanguageType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class PackRequestDto {

    @Getter
    public static class CreatePackDto {

        @Schema(example = "Test Pack")
        @NotBlank
        private String name;

        @Schema(example = "ENG")
        @NotNull
        private LanguageType srcLang;

        @Schema(example = "VIE")
        @NotNull
        private LanguageType desLang;

        private String description;

        private String keywords;

        private Boolean isPublished;
    }

    @Getter
    public static class UpdatePackDto {
        private String name;
        private String description;
        private String keywords;
        private LanguageType srcLang;
        private LanguageType desLang;
        private Boolean isPublished;
        private Boolean isDeleted;
    }

    @Getter
    public static class SavePackDto {
        @NotNull
        private Long packId;
    }

    @Getter
    public static class LikePackDto {
        @NotNull
        private Long packId;
    }
}
