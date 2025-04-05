package com.ndinhchien.langPractice.domain.pack.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ndinhchien.langPractice.domain.pack.dto.PackRequestDto.UpdatePackDto;
import com.ndinhchien.langPractice.domain.pack.entity.type.LanguageType;
import com.ndinhchien.langPractice.domain.question.entity.Question;
import com.ndinhchien.langPractice.domain.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "packCache")
@DynamicUpdate
@NoArgsConstructor
@Getter
@Entity
@Table(name = "packs", indexes = {
        @Index(name = "packs_owner_id_idx", columnList = "owner_id"),
        @Index(name = "packs_published_at_idx", columnList = "published_at"),
        @Index(name = "packs_save_count_idx", columnList = "save_count"),
})
public class Pack implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String keywords;

    @JsonIgnore
    @Column(columnDefinition = "tsvector", nullable = false, insertable = false)
    private String fulltext;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Set<Long> likes;

    @Column
    private Integer likeCount;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Set<Long> saves;

    @Column
    private Integer saveCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageType srcLang;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageType desLang;

    @Column(name = "owner_id", insertable = false, updatable = false)
    private Long ownerId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column
    private Integer total;

    @Column
    private Boolean isPublished;

    @Column
    private Boolean isDeleted;

    @Column
    private Instant publishedAt;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "pack", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Question> questions;

    @Builder
    public Pack(User owner, String name, String description, String keywords, LanguageType srcLang,
            LanguageType desLang) {
        this.owner = owner;
        this.ownerId = owner.getId();
        this.name = name;
        this.description = description;
        this.keywords = keywords;
        this.srcLang = srcLang;
        this.desLang = desLang;
    }

    @PrePersist
    private void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = Instant.now();
        }
        if (total == null) {
            total = 0;
        }
        if (likeCount == null) {
            likeCount = 0;
        }
        if (saveCount == null) {
            saveCount = 0;
        }
        if (likes == null) {
            likes = new HashSet<>();
        }
        if (saves == null) {
            saves = new HashSet<>();
        }
        if (isPublished == null) {
            isPublished = false;
        }
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void update(UpdatePackDto dto) {
        String name = dto.getName();
        String description = dto.getDescription();
        String keywords = dto.getKeywords();
        LanguageType srcLang = dto.getSrcLang();
        LanguageType desLang = dto.getDesLang();
        Boolean isPublished = dto.getIsPublished();
        Boolean isDeleted = dto.getIsDeleted();

        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
        if (keywords != null) {
            this.keywords = keywords;
        }
        if (srcLang != null) {
            this.srcLang = srcLang;
        }
        if (desLang != null) {
            this.desLang = desLang;
        }
        if (isPublished != null) {
            setIsPublished(isPublished);
        }
        if (isDeleted != null) {
            this.isDeleted = isDeleted;
        }
    }

    public boolean isOwner(User user) {
        return this.ownerId.equals(user.getId());
    }

    public boolean saveBy(User user) {
        if (this.saves.contains(user.getId())) {
            this.saves.remove(user.getId());
            this.saveCount = this.saves.size();
            return false;
        }
        this.saves.add(user.getId());
        this.saveCount = this.saves.size();
        return true;
    }

    public boolean likeBy(User user) {
        if (!this.likes.contains(user.getId())) {
            this.likes.add(user.getId());
            this.likeCount = this.likes.size();
            return true;
        }
        this.likes.remove(user.getId());
        this.likeCount = this.likes.size();
        return false;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
        if (isPublished == true && this.publishedAt == null) {
            this.publishedAt = Instant.now();
        }
    }

}
