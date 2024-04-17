package com.study.withus.user.infrastructure.persistence.repository.impl;

import com.study.withus.user.domain.entity.FriendShip;
import com.study.withus.user.domain.entity.User;
import com.study.withus.user.infrastructure.persistence.entity.FriendshipEntity;
import com.study.withus.user.infrastructure.persistence.entity.UserEntity;
import com.study.withus.user.infrastructure.persistence.repository.FriendshipJpaRepository;
import com.study.withus.user.domain.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FriendshipRepositoryImpl implements FriendshipRepository {

    private final FriendshipJpaRepository friendshipJpaRepository;

    @Override
    public Optional<FriendShip> findByUuid(String uuid) {
        return Optional.empty();
    }

    @Override
    public FriendShip save(FriendShip friendShip) {
        FriendshipEntity friendshipEntity = friendshipJpaRepository.save(FriendshipEntity.from(friendShip));
        return friendshipEntity.toModel();
    }

    @Override
    public boolean existsByRequestUserAndTargetUser(User requestUser, User targetUser) {
        return friendshipJpaRepository.existsByRequestUserAndTargetUser(UserEntity.from(requestUser), UserEntity.from(targetUser));
    }

    @Override
    public Optional<FriendShip> findByRequestUserAndTargetUser(User requestUser, User targetUser) {
        return friendshipJpaRepository.findByRequestUserAndTargetUser(UserEntity.from(requestUser), UserEntity.from(targetUser))
                .map(FriendshipEntity::toModel);
    }
}
