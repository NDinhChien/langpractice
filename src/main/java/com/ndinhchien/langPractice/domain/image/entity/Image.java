package com.ndinhchien.langPractice.domain.image.entity;

import java.io.IOException;
import java.time.Instant;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

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
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "imageCache")
@NoArgsConstructor
@Getter
@Entity
@Table(name = "images", indexes = {
        @Index(name = "images_owner_id_idx", columnList = "owner_id"),
})
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Long size;

    @JsonIgnore
    @Column(columnDefinition = "bytea")
    private byte[] data;

    @Column(name = "owner_id", insertable = false, updatable = false)
    private Long ownerId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column
    private Boolean isAvatar;

    @Column(nullable = false)
    private Instant uploadedAt;

    public Image(User owner, String name, String type, Long size, byte[] data, Boolean isAvatar) throws IOException {
        this.owner = owner;
        this.ownerId = owner.getId();
        this.name = name;
        this.type = type;
        this.size = size;
        this.data = data;
        this.isAvatar = isAvatar;
    }

    @PrePersist
    private void prePersist() {
        if (uploadedAt == null) {
            uploadedAt = Instant.now();
        }
        if (isAvatar == null) {
            isAvatar = false;
        }
    }
}
