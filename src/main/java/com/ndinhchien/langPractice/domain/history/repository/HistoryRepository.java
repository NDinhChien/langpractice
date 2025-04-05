package com.ndinhchien.langPractice.domain.history.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndinhchien.langPractice.domain.history.dto.HistoryResponseDto.IHistoryDto;
import com.ndinhchien.langPractice.domain.history.entity.History;
import com.ndinhchien.langPractice.domain.user.entity.User;

public interface HistoryRepository extends JpaRepository<History, Long> {

    <T> Optional<T> findByUserAndPackId(User user, Long packId, Class<T> type);

    List<IHistoryDto> findAllByUser(User user);

    Optional<History> findByUserAndPackId(User user, Long packId);
}
