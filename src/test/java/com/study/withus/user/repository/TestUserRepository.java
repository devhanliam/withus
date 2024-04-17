package com.study.withus.user.repository;

import com.study.withus.user.domain.entity.Friends;
import com.study.withus.user.domain.entity.User;
import com.study.withus.user.domain.repository.UserRepository;
import com.study.withus.util.exceptionhandler.exception.UserDuplicatedException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TestUserRepository implements UserRepository {
    public final Map<Long, User> concurrentMap = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1L);
    @Override
    public Optional<User> findByLoginId(String id) {
        Optional<User> optionalUser = concurrentMap.values().stream()
                .filter(u -> u.getLoginId().equals(id))
                .findFirst();
        return optionalUser;
    }

    @Override
    public User save(User user) {
        Optional<User> optionalUser = concurrentMap.values().stream()
                .filter(u -> u.getLoginId().equals(user.getLoginId()))
                .findFirst();
        if(optionalUser.isPresent())
            throw new UserDuplicatedException();
        concurrentMap.put(index.getAndIncrement(), user);
        return user;
    }

    @Override
    public Optional<User> findByUuid(String targetId) {
        return Optional.empty();
    }
}
