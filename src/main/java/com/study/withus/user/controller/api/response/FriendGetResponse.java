package com.study.withus.user.controller.api.response;

import com.study.withus.user.domain.entity.Friends;
import lombok.Data;

@Data
public class FriendGetResponse {

    private String uuid;
    private String username;

    public FriendGetResponse(String uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public static FriendGetResponse from(Friends friends) {
        return new FriendGetResponse(friends.getFriend().getUuid(),
                friends.getFriend().getUsername());
    }
}
