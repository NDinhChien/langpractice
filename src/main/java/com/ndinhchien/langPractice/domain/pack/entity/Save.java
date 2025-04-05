package com.ndinhchien.langPractice.domain.pack.entity;

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
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "saveCache")
@NoArgsConstructor
@Getter
@Entity
@Table(name = "saves", indexes = {
        @Index(name = "saves_user_id_idx", columnList = "user_id"),
})
public class Save {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pack_id", insertable = false, updatable = false)
    private Long packId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pack_id")
    private Pack pack;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Instant createdAt;

    @Column
    private Boolean isDeleted;

    public Save(User user, Pack pack) {
        this.user = user;
        this.userId = user.getId();
        this.pack = pack;
        this.packId = pack.getId();
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
