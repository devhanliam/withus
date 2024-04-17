package com.study.withus.user.infrastructure.persistence.repository;

import com.study.withus.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByLoginId(String loginId);
    Optional<UserEntity> findByUuid(String uuid);
    List<UserEntity> findByIsSearchable(boolean isSearchable);
}
