package com.study.withus.user.controller.api.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserNormalInfoModifyRequest {
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,12}$", message = "사용자 이름은 특수문자를 제외한 2~10자리여야 합니다.")
    private String username;
    private boolean isSearchable;
}
