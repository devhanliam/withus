package com.study.withus.user.infrastructure.persistence.entity;

import com.study.withus.user.domain.entity.FriendShip;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friendship")
public class FriendshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendship_id")
    private Long id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id"
            ,name = "target_user_id"
            ,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity targetUser;
    @ManyToOne
    @JoinColumn(name = "request_user_id"
            ,referencedColumnName = "user_id"
            ,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity requestUser;
    private boolean result;
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    public static FriendshipEntity from(FriendShip friendShip) {
        return FriendshipEntity.builder()
                .id(friendShip.getId())
                .requestUser(UserEntity.from(friendShip.getRequestUser()))
                .targetUser(UserEntity.from(friendShip.getTargetUser()))
                .createdDate(friendShip.getCreatedDate())
                .modifiedDate(friendShip.getModifiedDate())
                .result(friendShip.isResult())
                .build();
    }

    public FriendShip toModel() {
        return FriendShip.builder()
                .id(id)
                .requestUser(requestUser.toModel())
                .targetUser(targetUser.toModel())
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .result(result)
                .build();
    }


}
