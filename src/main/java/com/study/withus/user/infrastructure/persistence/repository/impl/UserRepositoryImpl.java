package com.study.withus.user.infrastructure.persistence.repository.impl;

import com.study.withus.user.domain.entity.User;
import com.study.withus.user.infrastructure.persistence.entity.UserEntity;
import com.study.withus.user.infrastructure.persistence.repository.UserJpaRepository;
import com.study.withus.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByLoginId(String id) {
        return userJpaRepository.findByLoginId(id).map(UserEntity::toModel);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.from(user)).toModel();
    }

    @Override
    public Optional<User> findByUuid(String targetId) {
        return userJpaRepository.findByUuid(targetId).map(UserEntity::toModel);
    }
}
