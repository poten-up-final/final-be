package com.dekk.domain.user.infrastructure.persistence;

import com.dekk.domain.user.domain.model.Provider;
import com.dekk.domain.user.domain.model.User;
import com.dekk.domain.user.domain.repository.UserRepository;
import com.dekk.domain.user.infrastructure.persistence.entity.UserJpaEntity;
import com.dekk.domain.user.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserMapper.toEntity(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByProviderAndProviderId(Provider provider, String providerId) {
        return jpaRepository.findByProviderAndProviderId(provider, providerId)
                .map(UserMapper::toDomain);
    }

    @Override
    public void delete(User user) {
        if (user.getId() != null) {
            jpaRepository.deleteById(user.getId());
        }
    }
}
