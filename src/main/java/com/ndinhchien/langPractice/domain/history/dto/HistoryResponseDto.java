package com.ndinhchien.langPractice.domain.history.dto;

import java.time.Instant;
import java.util.List;

import com.ndinhchien.langPractice.domain.history.entity.type.HistoryRecordType;

public class HistoryResponseDto {
    public interface IHistoryDto {
        Long getId();

        Long getUserId();

        Long getPackId();

        List<HistoryRecordType> getRecords();

        Instant getCreatedAt();

        Instant getUpdatedAt();
    }

    public record HistoryDto(
            Long id,
            Long userId,
            Long packId,
            List<HistoryRecordType> records,
            Instant createdAt,
            Instant updatedAt) {
    }

}
