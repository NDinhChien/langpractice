package com.ndinhchien.langPractice.domain.question.entity;

import java.time.Instant;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ndinhchien.langPractice.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "favouriteCache")
@NoArgsConstructor
@Getter
@Entity
@Table(name = "favourites", indexes = {
        @Index(name = "favourites_user_id_idx", columnList = "user_id"),
})
public class Favourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_id", insertable = false, updatable = false)
    private Long questionId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private Instant createdAt;

    public Favourite(User user, Question question) {
        this.user = user;
        this.userId = user.getId();
        this.question = question;
        this.questionId = question.getId();
        this.isDeleted = false;
    }

    @PrePersist
    private void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
