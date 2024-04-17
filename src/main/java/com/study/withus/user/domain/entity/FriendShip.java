package com.study.withus.user.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FriendShip {

    private Long id;
    private User requestUser;
    private User targetUser;
    private boolean result;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public FriendShip(Long id, User requestUser, User targetUser, boolean result, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.requestUser = requestUser;
        this.targetUser = targetUser;
        this.result = result;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static FriendShip create(User me, User friend) {
        LocalDateTime now = LocalDateTime.now();
        return FriendShip.builder()
                .requestUser(me)
                .targetUser(friend)
                .result(false)
                .createdDate(now)
                .modifiedDate(now)
                .build();
    }

    public void changeResult(boolean result) {
        this.result = result;
        changeModifiedDate();
    }

    private void changeModifiedDate() {
        this.modifiedDate = LocalDateTime.now();
    }
}
