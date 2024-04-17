package com.study.withus.user.infrastructure.persistence.repository;

import com.study.withus.user.infrastructure.persistence.entity.FriendsEntity;
import com.study.withus.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendsJpaRepository extends JpaRepository<FriendsEntity, Long> {
    List<FriendsEntity> findByMe(UserEntity user);
}
