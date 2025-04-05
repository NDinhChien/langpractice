package com.ndinhchien.langPractice.domain.pack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndinhchien.langPractice.domain.pack.entity.Pack;
import com.ndinhchien.langPractice.domain.pack.entity.Save;
import com.ndinhchien.langPractice.domain.user.entity.User;

public interface SaveRepository extends JpaRepository<Save, Long> {

    boolean existsByUserAndPack(User user, Pack pack);

    Optional<Save> findByUserAndPack(User user, Pack pack);
}
