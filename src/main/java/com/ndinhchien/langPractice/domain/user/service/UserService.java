package com.ndinhchien.langPractice.domain.user.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ndinhchien.langPractice.domain.image.dto.ImageResponseDto.IImageDto;
import com.ndinhchien.langPractice.domain.image.entity.Image;
import com.ndinhchien.langPractice.domain.image.service.ImageService;
import com.ndinhchien.langPractice.domain.question.dto.QuestionResponseDto.IQuestionDto;
import com.ndinhchien.langPractice.domain.question.service.QuestionService;
import com.ndinhchien.langPractice.domain.user.dto.UserRequestDto.UpdateProfileDto;
import com.ndinhchien.langPractice.domain.user.dto.UserResponseDto.IUserBasicDto;
import com.ndinhchien.langPractice.domain.user.dto.UserResponseDto.IUserDto;
import com.ndinhchien.langPractice.domain.user.dto.UserResponseDto.UserBasicDto;
import com.ndinhchien.langPractice.domain.user.entity.User;
import com.ndinhchien.langPractice.domain.user.repository.UserRepository;
import com.ndinhchien.langPractice.global.exception.BusinessException;
import com.ndinhchien.langPractice.global.exception.ErrorMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    public static final List<String> USERNAME_RESERVED_LIST = Arrays.asList("admin", "system");
    public static final int USERNAME_UPDATE_INTERVAL_DAYS = 30;
    private final ImageService imageService;
    private final QuestionService questionService;
    private final UserRepository userRepository;

    public IUserDto getProfile(Long id) {
        return userRepository.findUserById(id).orElseThrow(
                () -> {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_NOT_FOUND);
                });
    }

    @Transactional
    public User updateProfile(User user, UpdateProfileDto requestDto) {
        if (requestDto != null) {
            String userName = requestDto.getUserName();
            if (StringUtils.hasText(userName) && user.canUpdateUserName(USERNAME_UPDATE_INTERVAL_DAYS)
                    && !USERNAME_RESERVED_LIST.contains(userName) && !userRepository.existsByUserName(userName)) {
                user.updateUserName(userName);
            }
            user.updateProfile(requestDto);
            return userRepository.save(user);
        }
        return null;
    }

    @Transactional
    public Image updateAvatar(User user, MultipartFile avatar) {
        if (avatar != null && !avatar.isEmpty()) {
            Image image = imageService.uploadImage(user, avatar, true);
            user.updateAvatar(image);
            userRepository.save(user);
            return image;
        }
        return null;
    }

    public IImageDto getUserAvatar(User user, Long avatarId) {
        if (avatarId == null)
            return null;
        return imageService.getImage(user, avatarId);
    }

    @Transactional
    public List<IQuestionDto> getQuestions(IUserDto profile, Boolean full) {
        Instant lastGet = profile.getLastGet();
        List<Long> packIds = new ArrayList<>();
        packIds.addAll(profile.getSaves().stream().map(s -> s.getPackId()).toList());
        packIds.addAll(profile.getPacks().stream().map(p -> p.getId()).toList());
        if (packIds.size() < 1) {
            return new ArrayList<>();
        }
        userRepository.updateLastGet(profile.getId(), Instant.now());
        return questionService.getQuestions(packIds, full ? null : lastGet);
    }

    public List<UserBasicDto> getUsers(List<Long> userIds) {
        List<UserBasicDto> result = new ArrayList<>();
        for (Long id : userIds) {
            UserBasicDto user = userRepository.findById(id, UserBasicDto.class).orElse(null);
            if (user != null) {
                result.add(user);
            }
        }
        return result;
    }
}
