package com.study.withus.user.infrastructure.persistence.repository;

import com.study.withus.user.infrastructure.persistence.entity.FriendshipEntity;
import com.study.withus.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendshipJpaRepository extends JpaRepository<FriendshipEntity, Long> {
    boolean existsByRequestUserAndTargetUser(UserEntity requestUser, UserEntity targetUser);
    Optional<FriendshipEntity> findByRequestUserAndTargetUser(UserEntity requestUser, UserEntity targetUser);
}
