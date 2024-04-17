package com.study.withus.user.infrastructure.persistence.repository.impl;

import com.study.withus.user.domain.entity.Friends;
import com.study.withus.user.domain.entity.User;
import com.study.withus.user.infrastructure.persistence.entity.FriendsEntity;
import com.study.withus.user.infrastructure.persistence.entity.UserEntity;
import com.study.withus.user.infrastructure.persistence.repository.FriendsJpaRepository;
import com.study.withus.user.domain.repository.FriendsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FriendsRepositoryImpl implements FriendsRepository {
    private final FriendsJpaRepository friendsJpaRepository;

    @Override
    public Optional<Friends> findById(Long id) {
        return friendsJpaRepository.findById(id).map(FriendsEntity::toModel);
    }

    @Override
    public List<Friends> findByMe(User user) {
        return friendsJpaRepository.findByMe(UserEntity.from(user))
                .stream()
                .map(FriendsEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Friends save(Friends friends) {
        return friendsJpaRepository.save(FriendsEntity.from(friends)).toModel();
    }

    @Override
    public List<Friends> saveAll(List<Friends> friendsList) {
        List<FriendsEntity> friendsEntityList = friendsList.stream()
                .map(FriendsEntity::from)
                .collect(Collectors.toList());
        return friendsJpaRepository.saveAll(friendsEntityList).stream()
                .map(FriendsEntity::toModel)
                .collect(Collectors.toList());
    }
}
