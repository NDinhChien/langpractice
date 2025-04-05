package com.ndinhchien.langPractice.domain.pack.dto;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import com.ndinhchien.langPractice.domain.pack.entity.type.LanguageType;
import com.ndinhchien.langPractice.domain.question.dto.QuestionResponseDto.IQuestionDto;
import com.ndinhchien.langPractice.domain.user.dto.UserResponseDto.IUserBasicDto;

public class PackResponseDto {
    public static interface IPackBasicDto {
        Long getId();

        String getName();

        String getDescription();

        String getKeywords();

        Set<Long> getLikes();

        Integer getLikeCount();

        Set<Long> getSaves();

        Integer getSaveCount();

        LanguageType getSrcLang();

        LanguageType getDesLang();

        Long getOwnerId();

        IUserBasicDto getOwner();

        Integer getTotal();

        Boolean getIsPublished();

        Boolean getIsDeleted();

        Instant getCreatedAt();

        Instant getUpdatedAt();

        Instant getPublishedAt();
    }

    public interface IPackDto extends IPackBasicDto {

        List<IQuestionDto> getQuestions();

    }

    public record PackBasicDto(
            Long id,
            String name,
            String description,
            String keywords,
            Set<Long> likes,
            Integer likeCount,
            Set<Long> saves,
            Integer saveCount,
            LanguageType srcLang,
            LanguageType desLang,
            Long ownerId,
            Integer total,
            Boolean isPublished,
            Boolean isDeleted,
            Instant createdAt,
            Instant updatedAt,
            Instant publishedAt) {
    }

    public static interface IPackSearchDto {
        Long getId();

        String getName();

        String getDescription();

        String getKeywords();

        String getLikes();

        Integer getLikeCount();

        String getSaves();

        Integer getSaveCount();

        LanguageType getSrcLang();

        LanguageType getDesLang();

        Long getOwnerId();

        Integer getTotal();

        Boolean getIsPublished();

        Boolean getIsDeleted();

        Instant getCreatedAt();

        Instant getUpdatedAt();

        Instant getPublishedAt();

        Float getRank();
    }

    public interface ISaveDto {
        Long getId();

        IPackBasicDto getPack();

        Long getPackId();

        Long getUserId();

        Instant getCreatedAt();
    }

}
