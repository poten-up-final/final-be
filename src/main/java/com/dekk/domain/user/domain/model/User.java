package com.dekk.domain.user.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private Long id;

    private String email;

    private String nickname;

    private Role role;

    private Provider provider;

    private String providerId;

    private UserStatus status;

    private Double height;
    private Double weight;

    private Gender gender;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private User(String email, Provider provider, String providerId, UserStatus status) {
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.status = status;
        this.role = Role.MEMBER;
    }

    public static User createSocialUser(String email, Provider provider, String providerId) {
        return new User(email, provider, providerId, UserStatus.PENDING);
    }

    public void completeOnboarding(String nickname, Double height, Double weight, Gender gender) {
        this.nickname = nickname;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.status = UserStatus.ACTIVE;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateBodyInfo(Double height, Double weight, Gender gender) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public void deleteUser() {
        this.status = UserStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }

    // Public setters for Mapper use (infrastructure layer)
    public void setId(Long id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
