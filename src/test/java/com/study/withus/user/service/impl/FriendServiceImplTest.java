package com.study.withus.user.service.impl;

import com.study.withus.user.config.TestConfig;
import com.study.withus.user.controller.api.response.FriendGetResponse;
import com.study.withus.user.controller.api.response.FriendshipCreateResponse;
import com.study.withus.user.domain.entity.Friends;
import com.study.withus.user.domain.entity.User;
import com.study.withus.user.domain.repository.FriendsRepository;
import com.study.withus.user.domain.repository.FriendshipRepository;
import com.study.withus.user.domain.repository.UserRepository;
import com.study.withus.user.repository.TestFriendRepository;
import com.study.withus.user.repository.TestFriendshipRepository;
import com.study.withus.user.repository.TestUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FriendServiceImplTest {

    FriendsRepository friendsRepository;
    UserRepository userRepository;
    FriendshipRepository friendshipRepository;
    FriendServiceImpl friendService;

    @BeforeEach
    public void setRepository() {
        friendsRepository = new TestFriendRepository();
        userRepository = new TestUserRepository();
        friendshipRepository = new TestFriendshipRepository();
        List<String> testUsers = List.of("testuser", "testfriend1", "testfriend2", "testfriend3", "notfriend1");
        List<String> testFriends = List.of("testfriend1", "testfriend2", "testfriend3");
        testUsers.stream()
                .map(user -> User.builder()
                        .loginId(user)
                        .username("username" + user)
                        .build())
                .forEach(user -> userRepository.save(user));
        User user = User.builder().loginId("testuser").build();
        AtomicInteger index = new AtomicInteger(1);
        testFriends.stream()
                .map(s -> User.builder()
                        .loginId(s)
                        .username("username" + s)
                        .build())
                .map(f -> Friends.builder()
                        .me(user)
                        .friend(f)
                        .relationName("relation" + index.getAndIncrement()).build())
                .forEach(f -> friendsRepository.save(f));
        friendService = new FriendServiceImpl(friendsRepository, userRepository, friendshipRepository);
    }

    @DisplayName("친구 목록을 가져온다")
    @Test
    void getList() {
        //given
        String loginId = "testuser";
        List<String> testFriends = List.of("testfriend1", "testfriend2", "testfriend3");
        User user = User.builder().loginId("testuser").build();
        List<FriendGetResponse> collect = testFriends.stream()
                .map(s -> User.builder().loginId(s).build())
                .map(f -> Friends.builder()
                        .me(user)
                        .friend(f)
                        .build())
                .map(f -> FriendGetResponse.from(f))
                .collect(Collectors.toList());
        //when
        List<FriendGetResponse> result = friendService.getList(loginId);

        //then
        Assertions.assertThat(result.size()).isEqualTo(collect.size());
    }

    @DisplayName("친구요청을 보내고 이미 친구인 경우, 유저가 없는 경우 예외를 발생시킨다")
    @Test
    void requestFriendshipForException() {
        //given
        String requestId = "testuser";
        String targetId = "testfriend1";
        String notExistUser = "anonymous";

        //when then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->friendService.requestFriendship(targetId, requestId));
        Assertions.assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(()->friendService.requestFriendship(notExistUser, requestId));
    }

    @DisplayName("친구요청을 보내고 친구가 아닌 경우 요청이 만들어진다")
    @Test
    void requestFriendshipNormalCase() {
        //given
        String requestId = "testuser";
        String targetId = "notfriend1";
        User requestUser = userRepository.findByLoginId(requestId).get();
        User targetUser = userRepository.findByLoginId(targetId).get();

        //when
        FriendshipCreateResponse response = friendService.requestFriendship(targetId, requestId);
        boolean isExists = friendshipRepository.existsByRequestUserAndTargetUser(requestUser, targetUser);
        boolean isFriend = friendService.getList(requestId).stream()
                .filter(f -> f.getUsername().equals("username" + targetId))
                .findFirst()
                .isPresent();

        //then
        Assertions.assertThat(response.getUsername()).isEqualTo("username" + targetId);
        Assertions.assertThat(isExists).isTrue();
        Assertions.assertThat(isFriend).isFalse();
    }

    @DisplayName("친구요청을 보내고 상대가 친구 신청을 한 경우 바로 친구가 된다")
    @Test
    void requestFriendshipAlreadyExistRequest() {
        //given
        String requestId = "testuser";
        String targetId = "notfriend1";
        User requestUser = userRepository.findByLoginId(requestId).get();
        User targetUser = userRepository.findByLoginId(targetId).get();
        friendService.requestFriendship(requestId, targetId);

        //when
        friendService.requestFriendship(targetId, requestId);
        boolean isFriend = friendService.getList(requestId).stream()
                .filter(f -> f.getUsername().equals("username" + targetId))
                .findFirst()
                .isPresent();

        //then
        Assertions.assertThat(isFriend).isTrue();
    }


    @DisplayName("친구 요청을 수락하고 친구가 된다")
    @Test
    void acceptFriendship() {
        //given
        String requestId = "testuser";
        String targetId = "notfriend1";
        User requestUser = userRepository.findByLoginId(requestId).get();
        User targetUser = userRepository.findByLoginId(targetId).get();
        friendService.requestFriendship(requestId, targetId);

        //when
        friendService.acceptFriendship(targetId, requestId);
        boolean isFriend = friendService.getList(requestId).stream()
                .filter(f -> f.getUsername().equals("username" + targetId))
                .findFirst()
                .isPresent();

        //then
        Assertions.assertThat(isFriend).isTrue();
    }
}