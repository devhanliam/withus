package com.study.withus.user.app;

import com.study.withus.user.web.dto.request.SignupRequestDto;

public interface SignupService {
    void signup(SignupRequestDto dto);
}
