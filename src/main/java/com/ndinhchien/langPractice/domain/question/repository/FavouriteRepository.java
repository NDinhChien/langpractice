package com.ndinhchien.langPractice.domain.question.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndinhchien.langPractice.domain.question.entity.Favourite;
import com.ndinhchien.langPractice.domain.user.entity.User;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    Optional<Favourite> findByUserAndQuestionId(User user, Long questionId);
}
