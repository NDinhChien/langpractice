package com.ndinhchien.langPractice.domain.question.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class QuestionRequestDto {

    @Getter
    public static class AddQuestionDto {
        @NotBlank
        private String question;

        @NotBlank
        private String answer;
    }

    @Getter
    public static class UpdateQuestionDto {
        private Long id;

        private String question;

        private String answer;

        private Boolean isDeleted;
    }

    @Getter
    public static class AddFavouriteQuestion {

        @NotNull
        private Long questionId;

    }
}
