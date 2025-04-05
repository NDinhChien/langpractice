package com.ndinhchien.langPractice.domain.history.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ndinhchien.langPractice.domain.history.dto.HistoryRequestDto.AddHistoryRecordDto;
import com.ndinhchien.langPractice.domain.history.dto.HistoryResponseDto.IHistoryDto;
import com.ndinhchien.langPractice.domain.history.entity.History;
import com.ndinhchien.langPractice.domain.history.entity.type.HistoryRecordType;
import com.ndinhchien.langPractice.domain.history.repository.HistoryRepository;
import com.ndinhchien.langPractice.domain.user.entity.User;
import com.ndinhchien.langPractice.global.exception.BusinessException;
import com.ndinhchien.langPractice.global.exception.ErrorMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public History getHistory(User user, Long packId) {
        return validateHistory(user, packId);
    }

    public List<IHistoryDto> getUserHistories(User user) {
        return historyRepository.findAllByUser(user);
    }

    @Transactional
    public History addHistoryRecord(User user, AddHistoryRecordDto requestDto) {
        History history = historyRepository.findByUserAndPackId(user, requestDto.getPackId()).orElse(
                new History(user, requestDto.getPackId()));
        HistoryRecordType record = new HistoryRecordType(requestDto.getCompletedAt(), 1, requestDto.getTotal(),
                requestDto.getTimeSpent());
        history.addHistoryRecord(record);
        return historyRepository.save(history);
    }

    private History validateHistory(User user, Long packId) {
        return historyRepository.findByUserAndPackId(user, packId).orElseThrow(
                () -> {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.HISTORY_NOT_FOUND);
                });
    }

}
