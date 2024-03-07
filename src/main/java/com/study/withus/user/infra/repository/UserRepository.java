package com.study.withus.user.infra.repository;

import com.study.withus.user.infra.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByUuid(String uuid);
}
