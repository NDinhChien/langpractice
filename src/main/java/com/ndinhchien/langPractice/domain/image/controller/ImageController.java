package com.ndinhchien.langPractice.domain.image.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndinhchien.langPractice.domain.image.dto.ImageResponseDto.IImageDto;
import com.ndinhchien.langPractice.domain.image.service.ImageService;
import com.ndinhchien.langPractice.global.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Image", description = "Image Related APIs")
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
@RestController
public class ImageController {
    private final ImageService imageService;

    @Operation(summary = "Get avatars")
    @PostMapping("/many")
    public BaseResponse<List<IImageDto>> getAvatars(
            @RequestBody @Valid List<Long> imageIds) {
        return BaseResponse.success("Images", imageService.getImages(imageIds));
    }
}
