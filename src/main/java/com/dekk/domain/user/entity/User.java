package com.dekk.domain.user.entity;

import com.dekk.common.error.BusinessException;
import com.dekk.common.error.UserErrorCode;
import com.dekk.domain.user.model.Gender;
import com.dekk.domain.user.model.Provider;
import com.dekk.domain.user.model.Role;
import com.dekk.domain.user.model.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.dekk.common.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    public static User createSocialUser(String email, Provider provider, String providerId) {
        User user = new User();
        user.email = email;
        user.provider = provider;
        user.providerId = providerId;
        user.status = UserStatus.PENDING;
        user.role = Role.MEMBER;
        return user;
    }

    public void completeOnboarding(String nickname, Double height, Double weight, Gender gender) {
        validatePendingStatus();
        this.status = UserStatus.ACTIVE;
        this.profile = Profile.create(this, height, weight, nickname,gender);
    }

    public void updateProfileInfo(String nickname, Double height, Double weight, Gender gender) {
        if(this.profile != null) {
            this.profile.update(nickname, height, weight, gender);
        }
    }

    public void deleteUser() {
        this.status = UserStatus.DELETED;
        this.markAsDeleted();
    }

    private void validatePendingStatus() {
        if (this.status != UserStatus.PENDING) {
            throw new BusinessException(UserErrorCode.ALREADY_ONBOARDED);
        }
    }
}