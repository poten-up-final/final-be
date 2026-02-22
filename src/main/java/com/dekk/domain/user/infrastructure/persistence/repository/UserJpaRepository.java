package com.dekk.domain.user.infrastructure.persistence.repository;

import com.dekk.domain.user.domain.model.Provider;
import com.dekk.domain.user.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findByProviderAndProviderId(Provider provider, String providerId);
}
