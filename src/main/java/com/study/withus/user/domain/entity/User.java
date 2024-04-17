package com.study.withus.user.domain.entity;


import com.study.withus.user.controller.api.request.UserCreateRequest;
import com.study.withus.user.controller.api.request.UserNormalInfoModifyRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    private Long id;
    private String uuid;
    private String loginId;
    private String username;
    private String password;
    private String roles;
    private boolean isSearchable;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime lastLoginDate;
    private Set<Friends> friends;

    public static User create(UserCreateRequest dto, PasswordEncoder passwordEncoder) {
        LocalDateTime now = LocalDateTime.now();
        String newUuid = UUID.randomUUID().toString();
        return User.builder()
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .uuid(newUuid)
                .isSearchable(true)
                .createdDate(now)
                .modifiedDate(now)
                .roles("ROLE_USER").build();
    }

    public void modifyNormalInfo(UserNormalInfoModifyRequest dto) {
        this.username = dto.getUsername();
        this.isSearchable = dto.isSearchable();
        this.modifiedDate = LocalDateTime.now();
    }
}
