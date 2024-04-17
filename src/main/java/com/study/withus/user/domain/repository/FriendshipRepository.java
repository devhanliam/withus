package com.study.withus.user.domain.repository;

import com.study.withus.user.domain.entity.FriendShip;
import com.study.withus.user.domain.entity.User;

import java.util.Optional;

public interface FriendshipRepository {
    Optional<FriendShip> findByUuid(String uuid);
    FriendShip save(FriendShip friendShip);
    boolean existsByRequestUserAndTargetUser(User requestUser, User targetUser);
    Optional<FriendShip> findByRequestUserAndTargetUser(User requestUser, User targetUser);
}
