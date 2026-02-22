package com.dekk.domain.user.infrastructure.persistence;

import com.dekk.domain.user.domain.model.User;
import com.dekk.domain.user.infrastructure.persistence.entity.UserJpaEntity;

/**
 * Mapper for bidirectional conversion between domain User and UserJpaEntity.
 * Static utility class - no Spring component needed.
 * Located in infrastructure layer to maintain proper hexagonal architecture.
 */
public class UserMapper {

    private UserMapper() {
        // Utility class - prevent instantiation
    }

    /**
     * Converts JPA entity to domain model.
     * 
     * @param entity the JPA entity to convert
     * @return domain User, or null if entity is null
     */
    public static User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        // Use factory method to create base User
        User user = User.createSocialUser(
            entity.getEmail(),
            entity.getProvider(),
            entity.getProviderId()
        );

        // Set fields not handled by factory method using public setters
        user.setId(entity.getId());
        user.setNickname(entity.getNickname());
        user.setRole(entity.getRole());
        user.setStatus(entity.getStatus());
        user.setHeight(entity.getHeight());
        user.setWeight(entity.getWeight());
        user.setGender(entity.getGender());
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());
        user.setDeletedAt(entity.getDeletedAt());

        return user;
    }

    /**
     * Converts domain model to JPA entity.
     * 
     * @param user the domain User to convert
     * @return JPA entity, or null if user is null
     */
    public static UserJpaEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        // Use all-args constructor to create entity
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
