package com.study.withus.user.service;

import com.study.withus.user.controller.api.response.FriendGetResponse;
import com.study.withus.user.controller.api.response.FriendshipCreateResponse;
import com.study.withus.user.domain.entity.Friends;

import java.util.List;

public class TestFriendService implements FriendService {

    @Override
    public Friends create() {
        return null;
    }

    @Override
    public List<FriendGetResponse> getList(String loginId) {
        return null;
    }

    @Override
    public FriendshipCreateResponse requestFriendship(String targetId, String loginId) {
        return null;
    }

    @Override
    public void acceptFriendship(String targetId, String username) {

    }
}
