package com.study.withus.user.repository;

import com.study.withus.user.domain.entity.Friends;
import com.study.withus.user.domain.entity.User;
import com.study.withus.user.domain.repository.FriendsRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TestFriendRepository implements FriendsRepository {
    public final Map<Long,Friends> concurrentMap = new ConcurrentHashMap<>();
    private AtomicLong index = new AtomicLong(1);

    @Override
    public Optional<Friends> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Friends> findByMe(User user) {
        List<Friends> friendsList = concurrentMap.values()
                .stream()
                .filter(f -> f.getMe().getLoginId().equals(user.getLoginId()))
                .collect(Collectors.toList());
        return friendsList;
    }

    @Override
    public Friends save(Friends friends) {
        concurrentMap.put(index.getAndIncrement(), friends);
        return friends;
    }

    @Override
    public List<Friends> saveAll(List<Friends> friendsList) {
        friendsList.stream()
                .forEach(f -> concurrentMap.put(index.getAndIncrement(), f));
        return friendsList;
    }
}
