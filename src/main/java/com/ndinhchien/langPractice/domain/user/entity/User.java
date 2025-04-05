package com.ndinhchien.langPractice.domain.user.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ndinhchien.langPractice.domain.history.entity.History;
import com.ndinhchien.langPractice.domain.image.entity.Image;
import com.ndinhchien.langPractice.domain.pack.entity.Pack;
import com.ndinhchien.langPractice.domain.pack.entity.Save;
import com.ndinhchien.langPractice.domain.question.entity.Favourite;
import com.ndinhchien.langPractice.domain.user.dto.UserRequestDto.UpdateProfileDto;
import com.ndinhchien.langPractice.domain.user.entity.type.RoleType;
import com.ndinhchien.langPractice.global.util.CommonUtil;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "userCache")
@DynamicUpdate
@NoArgsConstructor
@Getter
@Entity
@Table(name = "users", indexes = {
        @Index(name = "users_user_name_idx", columnList = "user_name", unique = true),
})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @JsonIgnore
    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @Column(nullable = false)
    private String tokenSecret;

    @Column
    private Long avatarId;

    @Column
    private String fullName;

    @Column
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant lastUserNameUpdate;

    @Column(nullable = false)
    private Instant lastGet;

    @JsonIgnore
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Pack> packs;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Save> saves;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Favourite> favourites;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<History> histories;

    @JsonIgnore
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Image> images;

    @Builder
    public User(String userName, String email, String password, RoleType role) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @PrePersist
    private void prePersist() {
        if (role == null) {
            role = RoleType.LEARNER;
        }
        if (tokenSecret == null) {
            tokenSecret = CommonUtil.generateSecureString(30);
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (lastUserNameUpdate == null) {
            lastUserNameUpdate = Instant.now();
        }
        if (lastGet == null) {
            lastGet = Instant.now();
        }
    }

    public void logout() {
        this.tokenSecret = CommonUtil.generateSecureString(25);
    }

    public boolean canUpdateUserName(int days) {
        return this.lastUserNameUpdate.plus(days, ChronoUnit.DAYS).isBefore(Instant.now());
    }

    public void updateUserName(String userName) {
        this.userName = userName;
        this.lastUserNameUpdate = Instant.now();
    }

    public void updateAvatar(Image avatar) {
        if (avatar != null) {
            this.avatarId = avatar.getId();
        }
    }

    public void updateProfile(UpdateProfileDto dto) {
        String fullName = dto.getFullName();
        String bio = dto.getBio();
        if (fullName != null) {
            this.fullName = fullName;
        }
        if (bio != null) {
            this.bio = bio;
        }

    }
}
