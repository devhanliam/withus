package com.study.withus.user.service.impl;

import com.study.withus.user.controller.api.response.FriendGetResponse;
import com.study.withus.user.controller.api.response.FriendshipCreateResponse;
import com.study.withus.user.domain.entity.FriendShip;
import com.study.withus.user.domain.entity.Friends;
import com.study.withus.user.domain.entity.User;
import com.study.withus.user.domain.repository.FriendsRepository;
import com.study.withus.user.domain.repository.FriendshipRepository;
import com.study.withus.user.domain.repository.UserRepository;
import com.study.withus.user.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendsRepository friendsRepository;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Override
    public Friends create() {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FriendGetResponse> getList(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("세션이 만료되었거나 존재하지 않는 회원의 요청입니다"));
        List<Friends> friendsList = friendsRepository.findByMe(user);
        return friendsList.stream()
                .map(f -> FriendGetResponse.from(f))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public FriendshipCreateResponse requestFriendship(String targetId, String loginId) {
        User me = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("세션이 만료되었거나 존재하지 않는 회원의 요청입니다"));
        User target = userRepository.findByLoginId(targetId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원을 요청했습니다"));
        LocalDateTime now = LocalDateTime.now();
        FriendShip friendShip = FriendShip.create(me, target);

        if (friendshipRepository.existsByRequestUserAndTargetUser(friendShip.getRequestUser(), friendShip.getTargetUser())) {
            throw new IllegalArgumentException("이미 신청되었습니다");
        }

        boolean isAlreadyFriend = friendsRepository.findByMe(me).stream()
                .filter(f -> f.getFriend().getLoginId().equals(target.getLoginId()))
                .findFirst()
                .isPresent();

        if (isAlreadyFriend) {
            throw new IllegalArgumentException("이미 친구입니다");
        }

        FriendShip targetFriendship = FriendShip.create(target, me);
        if (friendshipRepository.existsByRequestUserAndTargetUser(targetFriendship.getRequestUser(), targetFriendship.getTargetUser())) {
            FriendShip requestOfFriend = friendshipRepository.findByRequestUserAndTargetUser(targetFriendship.getRequestUser(),targetFriendship.getTargetUser())
                    .orElseThrow(() -> new UsernameNotFoundException("친구 요청이 존재하지 않습니다"));
            requestOfFriend.changeResult(true);
            friendshipRepository.save(requestOfFriend);
            Friends friendsMe = Friends.create(me, target);
            Friends friendsTarget = Friends.create(target, me);
            List<Friends> friendsList = friendsRepository.saveAll(List.of(friendsMe, friendsTarget));
            String joinedUsername = friendsList.stream()
                    .map(f -> f.getMe().getUsername())
                    .collect(Collectors.joining(","));
            return new FriendshipCreateResponse(joinedUsername);
        }

        FriendShip save = friendshipRepository.save(friendShip);
        String username = save.getTargetUser().getUsername();
        return new FriendshipCreateResponse(username);
    }

    @Transactional
    @Override
    public void acceptFriendship(String targetId, String loginId) {
        User me = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("세션이 만료되었거나 존재하지 않는 회원의 요청입니다"));
        User target = userRepository.findByLoginId(targetId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원을 요청했습니다"));
        FriendShip friendShip = FriendShip.create(target, me);
        friendShip.changeResult(true);
        friendshipRepository.findByRequestUserAndTargetUser(friendShip.getRequestUser(), friendShip.getTargetUser())
                .orElseThrow(() -> new NoSuchElementException("존재하지않는 신청입니다"));
        Friends friendsMe = Friends.create(me, target);
        Friends friendsTarget = Friends.create(target, me);
        friendsRepository.saveAll(List.of(friendsMe, friendsTarget));
    }
}
