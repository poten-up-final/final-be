package com.dekk.domain.user.domain.repository;

import com.dekk.domain.user.domain.model.Provider;
import com.dekk.domain.user.domain.model.User;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);
    void delete(User user);
}
