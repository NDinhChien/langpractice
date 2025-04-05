package com.ndinhchien.langPractice.domain.history.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndinhchien.langPractice.domain.history.dto.HistoryRequestDto.AddHistoryRecordDto;
import com.ndinhchien.langPractice.domain.history.dto.HistoryResponseDto.IHistoryDto;
import com.ndinhchien.langPractice.domain.history.entity.History;
import com.ndinhchien.langPractice.domain.history.service.HistoryService;
import com.ndinhchien.langPractice.global.auth.UserDetailsImpl;
import com.ndinhchien.langPractice.global.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Validated
@Tag(name = "history", description = "History Related APIs")
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
@RestController
public class HistoryController {
    private final HistoryService historyService;

    @Operation(summary = "Get Pack Histories")
    @GetMapping("/packId/{packId}")
    BaseResponse<History> getHistory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable @Min(0) Long packId) {
        History history = historyService.getHistory(userDetails.getUser(), packId);
        return BaseResponse.success("Get History", history);
    }

    @Operation(summary = "Get All Histories")
    @GetMapping("/all")
    BaseResponse<List<IHistoryDto>> getHistories(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<IHistoryDto> result = historyService.getUserHistories(userDetails.getUser());
        return BaseResponse.success("User's History", result);
    }

    @Operation(summary = "Add History Record")
    @PostMapping("/record")
    BaseResponse<History> addHistoryRecord(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid AddHistoryRecordDto requestDto) {
        History updated = historyService.addHistoryRecord(userDetails.getUser(), requestDto);
        return BaseResponse.success("New record added", updated);
    }

}
