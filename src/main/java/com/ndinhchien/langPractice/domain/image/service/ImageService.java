package com.ndinhchien.langPractice.domain.image.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ndinhchien.langPractice.domain.image.dto.ImageResponseDto.IImageBasicDto;
import com.ndinhchien.langPractice.domain.image.dto.ImageResponseDto.IImageDto;
import com.ndinhchien.langPractice.domain.image.entity.Image;
import com.ndinhchien.langPractice.domain.image.repository.ImageRepository;
import com.ndinhchien.langPractice.domain.user.entity.User;
import com.ndinhchien.langPractice.global.exception.BusinessException;
import com.ndinhchien.langPractice.global.exception.ErrorMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {
    private static final int MAX_IMAGE_UPLOAD_SIZE = 1 * 1024 * 1024;
    private static final List<String> LIMIT_IMAGE_TYPE_LIST = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp");

    private final ImageRepository imageRepository;

    @Transactional
    public Image uploadImage(User user, MultipartFile file, Boolean isAvatar) {
        try {
            validateImage(file);
            String fileName = UUID.randomUUID() + getFileExtension(file.getOriginalFilename());
            Image image = new Image(user, fileName, file.getContentType(), file.getSize(),
                    file.getBytes(), isAvatar);
            return imageRepository.save(image);
        } catch (Exception e) {
            log.info("Failed to upload image");
        }
        return null;
    }

    public List<IImageBasicDto> getImages(User owner) {
        return imageRepository.findByOwner(owner);
    }

    public List<IImageDto> getImages(List<Long> imageIds) {
        List<IImageDto> result = new ArrayList<>();
        for (Long imageId : imageIds) {
            IImageDto image = imageRepository.findImageById(imageId).orElse(null);
            if (image == null)
                continue;
            result.add(image);
        }
        return result;
    }

    public IImageDto getImage(User user, Long id) {
        return imageRepository.findByIdAndOwner(id, user).orElseThrow(() -> {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.IMAGE_NOT_FOUND);
        });
    }

    private void validateImage(MultipartFile file) {
        if (!LIMIT_IMAGE_TYPE_LIST.contains(file.getContentType())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.UNSUPPORTED_FILE_TYPE);
        }
        if (file.getSize() > MAX_IMAGE_UPLOAD_SIZE) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.FILE_SIZE_EXCEEDED);
        }
    }

    private String getFileExtension(String fileName) {
        int indexOfLastDot = fileName.lastIndexOf('.');
        if (indexOfLastDot == -1) {
            return "";
        }
        return fileName.substring(indexOfLastDot);
    }
}
