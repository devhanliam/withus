package com.study.withus.user.infrastructure.persistence.entity;


import com.study.withus.user.domain.entity.Friends;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "friends")
public class FriendsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friends_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "friend_id",referencedColumnName = "user_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity friend;
    @ManyToOne
    @JoinColumn(name = "me_id",referencedColumnName = "user_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity me;
    private String relationName;
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Friends toModel() {
        return Friends.builder()
                .id(id)
                .me(me.toModel())
                .friend(friend.toModel())
                .relationName(relationName)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .build();
    }

    public static FriendsEntity from(Friends friends) {
        return FriendsEntity.builder()
                .id(friends.getId())
                .friend(UserEntity.from(friends.getFriend()))
                .me(UserEntity.from(friends.getMe()))
                .relationName(friends.getRelationName())
                .createdDate(friends.getCreatedDate())
                .modifiedDate(friends.getModifiedDate())
                .build();

    }
}
