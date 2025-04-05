package com.ndinhchien.langPractice.domain.image.dto;

import java.time.Instant;

public class ImageResponseDto {
    public static interface IImageBasicDto {
        Long getId();

        String getName();

        String getType();

        Long getOwnerId();

        Instant getUploadedAt();
    }

    public static interface IImageDto extends IImageBasicDto {
        byte[] getData();
    }

    public record ImageBasicDto(
            Long id,
            String name,
            String type,
            Long ownerId,
            Instant uploadedAt) {
    }
}
