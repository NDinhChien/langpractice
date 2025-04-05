package com.ndinhchien.langPractice.domain.user.dto;

import java.time.Instant;
import java.util.List;

import com.ndinhchien.langPractice.domain.history.dto.HistoryResponseDto.IHistoryDto;
import com.ndinhchien.langPractice.domain.pack.dto.PackResponseDto.IPackBasicDto;
import com.ndinhchien.langPractice.domain.pack.dto.PackResponseDto.ISaveDto;
import com.ndinhchien.langPractice.domain.question.dto.QuestionResponseDto.IFavouriteQuestionDto;
import com.ndinhchien.langPractice.domain.user.entity.type.RoleType;

public class UserResponseDto {
    public static interface IUserBasicDto {
        Long getId();

        String getUserName();

        Long getAvatarId();

        String getFullName();

        String getBio();

        RoleType getRole();

        Instant getCreatedAt();

        Instant getLastUserNameUpdate();

    }

    public static interface IUserDto extends IUserBasicDto {

        List<IPackBasicDto> getPacks();

        List<ISaveDto> getSaves();

        List<IHistoryDto> getHistories();

        List<IFavouriteQuestionDto> getFavourites();

        Instant getLastGet();
    }

    public static record UserBasicDto(
            Long id,
            String userName,
            Long avatarId,
            String fullName,
            String bio,
            RoleType role,
            Instant createdAt,
            Instant lastUserNameUpdate) {
    }

}
