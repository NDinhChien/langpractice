package com.ndinhchien.langPractice.domain.question.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ndinhchien.langPractice.domain.pack.entity.Pack;
import com.ndinhchien.langPractice.domain.question.dto.QuestionResponseDto.IQuestionDto;
import com.ndinhchien.langPractice.domain.question.dto.QuestionResponseDto.QuestionDto;
import com.ndinhchien.langPractice.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    public final String FIND_QUESTIONS_QUERY = """
                SELECT id, packId, question, answer, isDeleted, createdAt, updatedAt
                FROM Question
                WHERE id IN :packIds AND updatedAt >= :updatedAt
            """;

    @Query(value = FIND_QUESTIONS_QUERY)
    List<IQuestionDto> findInPacksAndUpdatedAtBefore(List<Long> packIds, Instant updatedAt);

    List<QuestionDto> findAllByPack(Pack pack);

}
