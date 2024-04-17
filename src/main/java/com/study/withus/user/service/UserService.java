package com.study.withus.user.service;

import com.study.withus.user.controller.api.request.UserNormalInfoModifyRequest;
import com.study.withus.user.domain.entity.User;
import com.study.withus.user.controller.api.request.UserCreateRequest;
import com.study.withus.util.security.SecurityUser;

public interface UserService {
    User create(UserCreateRequest dto);

    User modify(UserNormalInfoModifyRequest dto, String loginId);

}
