package com.ndinhchien.langPractice.domain.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ndinhchien.langPractice.domain.image.dto.ImageResponseDto.IImageDto;
import com.ndinhchien.langPractice.domain.image.entity.Image;
import com.ndinhchien.langPractice.domain.question.dto.QuestionResponseDto.IQuestionDto;
import com.ndinhchien.langPractice.domain.user.dto.UserRequestDto.UpdateProfileDto;
import com.ndinhchien.langPractice.domain.user.dto.UserResponseDto.IUserDto;
import com.ndinhchien.langPractice.domain.user.dto.UserResponseDto.UserBasicDto;
import com.ndinhchien.langPractice.domain.user.entity.User;
import com.ndinhchien.langPractice.domain.user.service.UserService;
import com.ndinhchien.langPractice.global.auth.UserDetailsImpl;
import com.ndinhchien.langPractice.global.dto.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "user", description = "User Related APIs")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get profile")
    @GetMapping("/profile")
    BaseResponse<Map<String, Object>> getProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "false") Boolean full) {
        IUserDto profile = userService.getProfile(userDetails.getUser().getId());
        IImageDto avatarImage = userService.getUserAvatar(userDetails.getUser(), profile.getAvatarId());
        List<IQuestionDto> questions = userService.getQuestions(profile, full);
        return BaseResponse.success("User profile",
                Map.of("profile", profile,
                        "email", userDetails.getUser().getEmail(),
                        "questions", questions,
                        "avatar", avatarImage == null ? "" : avatarImage.getData()));
    }

    @Operation(summary = "Update profile")
    @PutMapping(path = "/profile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    BaseResponse<Map<String, Object>> updateProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(name = "profile", required = false) UpdateProfileDto requestDto,
            @RequestPart(name = "avatar", required = false) MultipartFile avatar) {
        User user = userService.updateProfile(userDetails.getUser(), requestDto);
        Image avatarImage = userService.updateAvatar(user, avatar);
        return BaseResponse.success("Update profile",
                Map.of("profile", user,
                        "avatar", avatarImage == null ? "" : avatarImage.getData()));
    }

    @Operation(summary = "Get users")
    @PostMapping(path = "/many")
    BaseResponse<List<UserBasicDto>> getUsers(
            @RequestBody @Valid List<Long> userIds) {
        return BaseResponse.success("Users", userService.getUsers(userIds));
    }

}
