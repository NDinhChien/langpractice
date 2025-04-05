package com.ndinhchien.langPractice.domain.question.dto;

import java.time.Instant;

public class QuestionResponseDto {

    public interface IQuestionDto {
        Long getId();

        Long getPackId();

        String getQuestion();

        String getAnswer();

        Boolean getIsDeleted();

        Instant getCreatedAt();

        Instant getUpdatedAt();
    }

    public record QuestionDto(
            Long id,
            Long packId,
            String question,
            String answer,
            Boolean isDeleted,
            Instant createdAt,
            Instant updatedAt) {
    };

    public interface IFavouriteQuestionDto {
        Long getId();

        Long getQuestionId();

        IQuestionDto getQuestion();

        Long getUserId();

        Instant getCreatedAt();
    }

    public record FavouriteQuestionDto(
            Long id,
            Long questionId,
            Long userId,
            Instant createdAt) {
    }
}
