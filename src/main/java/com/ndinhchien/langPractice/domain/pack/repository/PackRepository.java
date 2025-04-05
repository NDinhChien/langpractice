package com.ndinhchien.langPractice.domain.pack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ndinhchien.langPractice.domain.pack.dto.PackResponseDto.IPackBasicDto;
import com.ndinhchien.langPractice.domain.pack.dto.PackResponseDto.IPackDto;
import com.ndinhchien.langPractice.domain.pack.dto.PackResponseDto.IPackSearchDto;
import com.ndinhchien.langPractice.domain.pack.entity.Pack;
import com.ndinhchien.langPractice.domain.user.entity.User;

public interface PackRepository extends JpaRepository<Pack, Long> {

        static final String SEARCH_QUERY = """
                        SELECT   id,
                                 name,
                                 description,
                                 keywords,
                                 like_count as likeCount,
                                 save_count as saveCount,
                                 saves,
                                 likes,
                                 src_lang as srcLang,
                                 des_lang as desLang,
                                 owner_id as ownerId,
                                 total,
                                 is_published as isPublished,
                                 is_deleted as isDeleted,
                                 published_at as publishedAt,
                                 created_at as createdAt,
                                 updated_at as updatedAt,
                                 ts_rank(fulltext, to_tsquery('english', :keywords)) AS rank
                        FROM     packs
                        WHERE    is_published = true AND is_deleted = false AND fulltext @@ to_tsquery('english', :keywords)
                        """;

        @Query(value = SEARCH_QUERY, nativeQuery = true)
        Page<IPackSearchDto> searchByKeywords(String keywords, Pageable pageable);

        <T> Optional<T> findById(Long id, Class<T> type);

        Optional<Pack> findByIdAndOwner(Long id, User owner);

        <T> List<T> findAllByOwner(User owner, Class<T> type);

        List<IPackDto> findAllByOwner(User owner);

        List<IPackBasicDto> findAllByOwnerAndIsPublishedAndIsDeleted(User owner, Boolean isPublished,
                        Boolean isDeleted);

        Optional<IPackDto> findPackById(Long id);

        Page<IPackBasicDto> findAllByIsPublishedAndIsDeleted(Pageable pageable, Boolean isPublished, Boolean isDeleted);

}
