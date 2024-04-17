package com.study.withus.user.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Friends {

    private Long id;
    private User friend;
    private User me;
    private String relationName;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public Friends(Long id, User friend, User me, String relationName, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.friend = friend;
        this.me = me;
        this.relationName = relationName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static Friends create(User me, User friend) {
        LocalDateTime now = LocalDateTime.now();
        return Friends.builder()
                .me(me)
                .friend(friend)
                .createdDate(now)
                .modifiedDate(now)
                .build();
    }
}
