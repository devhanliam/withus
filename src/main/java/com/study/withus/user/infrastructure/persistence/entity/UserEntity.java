package com.study.withus.user.infrastructure.persistence.entity;

import com.study.withus.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true,updatable = false)
    private String uuid;
    @Column(unique = true,updatable = false)
    private String loginId;
    private String username;
    private String password;
    private String roles;
    private boolean isSearchable;
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime lastLoginDate;

    public User toModel() {
        return User.builder()
                .id(id)
                .loginId(loginId)
                .uuid(uuid)
                .username(username)
                .password(password)
                .roles(roles)
                .isSearchable(isSearchable)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .lastLoginDate(lastLoginDate)
                .build();
    }

    public static UserEntity from(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.id = user.getId();
        userEntity.loginId = user.getLoginId();
        userEntity.uuid = user.getUuid();
        userEntity.username = user.getUsername();
        userEntity.password = user.getPassword();
        userEntity.roles = user.getRoles();
        userEntity.isSearchable = user.isSearchable();
        userEntity.createdDate = user.getModifiedDate();
        userEntity.lastLoginDate = user.getLastLoginDate();
        return userEntity;
    }
}
