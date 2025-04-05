package com.ndinhchien.langPractice.domain.question.entity;

import java.io.Serializable;
import java.time.Instant;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ndinhchien.langPractice.domain.pack.entity.Pack;
import com.ndinhchien.langPractice.domain.question.dto.QuestionRequestDto.UpdateQuestionDto;

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

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "questionCache")
@DynamicUpdate
@NoArgsConstructor
@Getter
@Entity
@Table(name = "questions", indexes = {
        @Index(name = "questions_pack_id_idx", columnList = "pack_id"),
        @Index(name = "questions_updated_at_idx", columnList = "updated_at"),
})
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pack_id", insertable = false, updatable = false)
    private Long packId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pack_id")
    private Pack pack;

    @Column(nullable = false)
    private Long ownerId;

    @Column(columnDefinition = "text", nullable = false)
    private String question;

    @Column(columnDefinition = "text", nullable = false)
    private String answer;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public Question(Pack pack, String question, String answer) {
        this.pack = pack;
        this.packId = pack.getId();
        this.ownerId = pack.getOwnerId();
        this.question = question;
        this.answer = answer;
    }

    @PrePersist
    private void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = Instant.now();
        }
        if (question == null) {
            question = "";
        }
        if (answer == null) {
            answer = "";
        }
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

    public void update(UpdateQuestionDto dto) {
        String question = dto.getQuestion();
        String answer = dto.getAnswer();
        Boolean isDeleted = dto.getIsDeleted();
        if (question != null) {
            this.question = question;
        }
        if (answer != null) {
            this.answer = answer;
        }
        if (isDeleted != null) {
            this.isDeleted = isDeleted;
        }
        this.updatedAt = Instant.now();
    }
}
