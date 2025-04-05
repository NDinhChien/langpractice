package com.ndinhchien.langPractice.domain.history.dto;

import java.time.Instant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class HistoryRequestDto {

    @Getter
    public static class AddHistoryRecordDto {

        @NotNull
        @Min(0)
        Long packId;

        @NotNull
        Instant completedAt;

        @NotNull
        Integer total;

        Integer timeSpent;
    }
}
