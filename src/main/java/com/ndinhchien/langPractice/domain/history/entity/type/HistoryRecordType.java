package com.ndinhchien.langPractice.domain.history.entity.type;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HistoryRecordType {
    private Instant completedAt;
    private Integer score;
    private Integer total;
    private Integer timeSpent;
}
