package com.study.withus.user.service;

import com.study.withus.user.controller.api.response.FriendGetResponse;
import com.study.withus.user.controller.api.response.FriendshipCreateResponse;
import com.study.withus.user.domain.entity.Friends;
import com.study.withus.util.security.SecurityUser;

import java.util.List;

public interface FriendService {
    Friends create();
    List<FriendGetResponse> getList(String loginId);
    FriendshipCreateResponse requestFriendship(String targetId, String loginId);
    void acceptFriendship(String targetId, String username);
}
