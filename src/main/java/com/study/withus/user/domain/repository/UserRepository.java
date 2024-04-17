package com.study.withus.user.domain.repository;

import com.study.withus.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByLoginId(String id);

    User save(User user);

    Optional<User> findByUuid(String targetId);
}
