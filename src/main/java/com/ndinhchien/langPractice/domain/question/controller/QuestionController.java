package com.ndinhchien.langPractice.domain.question.controller;

import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ndinhchien.langPractice.domain.question.dto.QuestionRequestDto.AddQuestionDto;
import com.ndinhchien.langPractice.domain.question.dto.QuestionRequestDto.AddFavouriteQuestion;
import com.ndinhchien.langPractice.domain.question.dto.QuestionRequestDto.UpdateQuestionDto;
import com.ndinhchien.langPractice.domain.question.dto.QuestionResponseDto.QuestionDto;
import com.ndinhchien.langPractice.domain.question.entity.Question;
import com.ndinhchien.langPractice.domain.question.entity.Favourite;
import com.ndinhchien.langPractice.domain.question.service.QuestionService;
import com.ndinhchien.langPractice.global.auth.UserDetailsImpl;
import com.ndinhchien.langPractice.global.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Validated
@Tag(name = "question", description = "Question Related APIs")
@RequiredArgsConstructor
@RequestMapping("/api/v1/question")
@RestController
public class QuestionController {
    private final QuestionService questionService;

    @Operation(summary = "Get pack's questions")
    @GetMapping("/packId/{packId}")
    BaseResponse<List<QuestionDto>> getQuestions(
            @AuthenticationPrincipal @Nullable UserDetailsImpl userDetails,
            @PathVariable @Min(0) Long packId) {
        List<QuestionDto> result = questionService.getQuestions(userDetails != null ? userDetails.getUser() : null,
                packId);
        return BaseResponse.success("Pack's Questions", result);
    }

    @Operation(summary = "Add questions to pack")
    @PostMapping("/packId/{packId}")
    BaseResponse<List<Question>> addQuestions(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable @Min(0) Long packId,
            @RequestBody List<@Valid AddQuestionDto> requestDto) {
        List<Question> result = questionService.addQuestions(userDetails.getUser(), packId, requestDto);
        return BaseResponse.success("Added", result);
    }

    @Operation(summary = "Update question")
    @PutMapping
    BaseResponse<Question> updateQuestion(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam @Min(0) Long questionId,
            @RequestBody @Valid UpdateQuestionDto requestDto) {
        Question updated = questionService.updateQuestion(userDetails.getUser(), questionId, requestDto);
        return BaseResponse.success("Update Question", updated);
    }

    @Operation(summary = "Update questions")
    @PutMapping("/many")
    BaseResponse<List<Question>> updateQuestions(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody List<@Valid UpdateQuestionDto> requestDto) {
        List<Question> updated = questionService.updateQuestions(userDetails.getUser(), requestDto);
        return BaseResponse.success("Update Questions", updated);
    }

    @Operation(summary = "Add or Remove Favourite Question")
    @PutMapping("/favourite")
    BaseResponse<Favourite> toggleFavourite(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody @Valid AddFavouriteQuestion requestDto) {
        Favourite updated = questionService.toggleFavourite(userDetails.getUser(), requestDto.getQuestionId());
        return BaseResponse.success("Favourite updated", updated);
    }

}
