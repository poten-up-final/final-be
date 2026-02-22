package com.dekk.domain.user.domain.model;

import com.dekk.global.common.model.BaseTimeModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeModel {

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

    public User(Long id, String email, String nickname, Role role, Provider provider, String providerId, UserStatus status, Double height, Double weight, Gender gender, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.status = status;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

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
        super.markAsDeleted();
    }
}