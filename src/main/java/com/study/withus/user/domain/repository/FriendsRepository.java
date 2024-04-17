package com.study.withus.user.domain.repository;

import com.study.withus.user.domain.entity.Friends;
import com.study.withus.user.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface FriendsRepository {
    Optional<Friends> findById(Long id);
    List<Friends> findByMe(User user);
    Friends save(Friends friends);
    List<Friends> saveAll(List<Friends> friendsList);

}
