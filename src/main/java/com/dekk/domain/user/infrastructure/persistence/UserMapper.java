package com.dekk.domain.user.infrastructure.persistence;

import com.dekk.domain.user.domain.model.User;
import com.dekk.domain.user.infrastructure.persistence.entity.UserJpaEntity;

public class UserMapper {

    private UserMapper() {
    }

    public static User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getRole(),
                entity.getProvider(),
                entity.getProviderId(),
                entity.getStatus(),
                entity.getHeight(),
                entity.getWeight(),
                entity.getGender(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

    public static UserJpaEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserJpaEntity(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                user.getProvider(),
                user.getProviderId(),
                user.getStatus(),
                user.getHeight(),
                user.getWeight(),
                user.getGender(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt()
        );
    }
}