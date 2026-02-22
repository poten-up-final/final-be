package com.dekk.domain.user.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.dekk.domain.user.domain.model.Gender;
import com.dekk.domain.user.domain.model.Provider;
import com.dekk.domain.user.domain.model.Role;
import com.dekk.domain.user.domain.model.UserStatus;
import com.dekk.global.common.entity.BaseTimeEntity;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String nickname;

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

    private Double height;
    private Double weight;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public UserJpaEntity(Long id, String email, String nickname, Role role, Provider provider, String providerId, UserStatus status, Double height, Double weight, Gender gender, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
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
}