package com.ndinhchien.langPractice.domain.user.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ndinhchien.langPractice.domain.user.dto.UserResponseDto.IUserDto;
import com.ndinhchien.langPractice.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<IUserDto> findUserById(Long id);

    <T> Optional<T> findById(Long id, Class<T> type);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    @Modifying
    @Query("UPDATE User SET lastGet = :lastGet WHERE id = :id")
    void updateLastGet(Long id, Instant lastGet);
}
