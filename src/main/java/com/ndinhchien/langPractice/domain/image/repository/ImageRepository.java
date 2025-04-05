package com.ndinhchien.langPractice.domain.image.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ndinhchien.langPractice.domain.image.dto.ImageResponseDto.IImageBasicDto;
import com.ndinhchien.langPractice.domain.image.dto.ImageResponseDto.IImageDto;
import com.ndinhchien.langPractice.domain.image.entity.Image;
import com.ndinhchien.langPractice.domain.user.entity.User;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<IImageBasicDto> findByOwner(User owner);

    Optional<IImageDto> findByIdAndOwner(Long id, User owner);

    Optional<IImageDto> findImageById(Long id);

    Optional<IImageDto> findImageByIdAndIsAvatar(Long id, Boolean isAvatar);
}
