package com.study.withus.user.controller.api.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserCreateRequest {

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,12}$", message = "아이디는 특수문자를 제외한 2~10자리여야 합니다.")
    private String loginId;
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,12}$", message = "사용자 이름은 특수문자를 제외한 2~10자리여야 합니다.")
    private String username;

}
