package com.ndinhchien.langPractice.domain.pack.controller;

import java.util.List;
import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndinhchien.langPractice.domain.pack.dto.PackRequestDto.CreatePackDto;
import com.ndinhchien.langPractice.domain.pack.dto.PackRequestDto.LikePackDto;
import com.ndinhchien.langPractice.domain.pack.dto.PackRequestDto.SavePackDto;
import com.ndinhchien.langPractice.domain.pack.dto.PackRequestDto.UpdatePackDto;
import com.ndinhchien.langPractice.domain.pack.entity.Pack;
import com.ndinhchien.langPractice.domain.pack.entity.Save;
import com.ndinhchien.langPractice.domain.pack.service.PackService;
import com.ndinhchien.langPractice.global.auth.UserDetailsImpl;
import com.ndinhchien.langPractice.global.dto.BaseResponse;
import com.ndinhchien.langPractice.global.dto.PageDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@Tag(name = "pack", description = "Pack Related APIs")
@RequiredArgsConstructor
@RequestMapping("/api/v1/pack")
@RestController
public class PackController {

    private final PackService packService;

    @Operation(summary = "Get pack by Id")
    @GetMapping
    BaseResponse<Object> getById(
            @AuthenticationPrincipal @Nullable UserDetailsImpl userDetails,
            @RequestParam @Min(0) long packId,
            @RequestParam(defaultValue = "false") boolean full) {

        Object pack = packService.getPackById(userDetails != null ? userDetails.getUser() : null, packId, full);
        return BaseResponse.success("Get pack by id", pack);
    }

    @Operation(summary = "Get user packs")
    @GetMapping("/all")
    BaseResponse<List<?>> getUserPacks(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "false") boolean full) {
        List<?> result = packService.getPacks(userDetails.getUser(), full);
        return BaseResponse.success("User packs", result);
    }

    @Operation(summary = "Get published packs")
    @PostMapping("/published/many")
    BaseResponse<Map<String, Object>> getPublishedPacks(
            @RequestBody @Valid List<Long> userIds) {
        Map<String, Object> result = packService.getPublishedPacksOfUsers(userIds);
        return BaseResponse.success("Users Published packs", result);
    }

    @Operation(summary = "Create pack")
    @PostMapping
    BaseResponse<Pack> create(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid CreatePackDto requestDto) {
        Pack pack = packService.createPack(userDetails.getUser(), requestDto);
        return BaseResponse.success("Create pack", pack);
    }

    @Operation(summary = "Update pack")
    @PutMapping
    BaseResponse<Pack> updatePack(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam @Min(0) Long packId,
            @RequestBody @Valid UpdatePackDto requestDto) {
        Pack updated = packService.updatePack(userDetails.getUser(), packId, requestDto);
        return BaseResponse.success("Update pack", updated);
    }

    @Operation(summary = "Save or Undo save pack")
    @PutMapping("/save")
    BaseResponse<Save> toggleSavePack(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid SavePackDto requestDto) {
        Save created = packService.toggleSave(userDetails.getUser(), requestDto.getPackId());
        return BaseResponse.success("Save pack", created);
    }

    @Operation(summary = "Like or Undo like pack")
    @PutMapping("/like")
    BaseResponse<Map<String, Object>> toggleLikePack(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid LikePackDto requestDto) {
        Boolean liked = packService.toggleLike(userDetails.getUser(), requestDto.getPackId());
        return BaseResponse.success("Like pack", Map.of("liked", liked));
    }

    @Operation(summary = "Search by keywords")
    @GetMapping("/search")
    BaseResponse<PageDto<?>> searchPacks(
            @RequestParam(defaultValue = "") String keywords,
            @RequestParam(defaultValue = "6") @Min(6) int pageSize,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "publishedAt") @Pattern(regexp = "^(publishedAt|saveCount)$") String sortBy,
            @RequestParam(defaultValue = "desc") @Pattern(regexp = "^(asc|desc)$") String sortOrder) {
        PageDto<?> result = packService.search(keywords, pageSize, page, sortBy, sortOrder);
        return BaseResponse.success("Search packs", result);
    }

}
