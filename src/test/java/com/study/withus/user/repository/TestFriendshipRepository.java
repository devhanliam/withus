package com.study.withus.user.repository;

import com.study.withus.user.domain.entity.FriendShip;
import com.study.withus.user.domain.entity.Friends;
import com.study.withus.user.domain.entity.User;
import com.study.withus.user.domain.repository.FriendshipRepository;
import com.study.withus.user.infrastructure.persistence.entity.FriendshipEntity;
import com.study.withus.user.infrastructure.persistence.entity.UserEntity;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestFriendshipRepository implements FriendshipRepository {
    public final Map<Long, FriendShip> concurrentMap = new ConcurrentHashMap<>();
    private AtomicLong index = new AtomicLong(1L);

    @Override
    public Optional<FriendShip> findByUuid(String uuid) {
        return Optional.empty();
    }

    @Override
    public FriendShip save(FriendShip friendShip) {
        concurrentMap.put(index.getAndIncrement(), friendShip);
        return friendShip;
    }

    @Override
    public boolean existsByRequestUserAndTargetUser(User requestUser, User targetUser) {
        Optional<FriendShip> friendShip = concurrentMap.values().stream()
                .filter(f -> f.getRequestUser().getLoginId().equals(requestUser.getLoginId()) && f.getTargetUser().getLoginId().equals(targetUser.getLoginId()))
                .findFirst();
        return friendShip.isPresent();
    }

    @Override
    public Optional<FriendShip> findByRequestUserAndTargetUser(User requestUser, User targetUser) {
        Optional<FriendShip> friendShip = concurrentMap.values().stream()
                .filter(f -> f.getRequestUser().getLoginId().equals(requestUser.getLoginId()) && f.getTargetUser().getLoginId().equals(targetUser.getLoginId()))
                .findFirst();
        return friendShip;
    }
}
