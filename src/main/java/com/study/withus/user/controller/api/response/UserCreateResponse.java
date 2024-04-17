package com.study.withus.user.controller.api.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreateResponse {
    String username;
    String loginId;
    String uuid;
}
