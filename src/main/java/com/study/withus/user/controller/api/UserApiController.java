package com.study.withus.user.controller.api;

import com.study.withus.user.controller.api.request.UserNormalInfoModifyRequest;
import com.study.withus.user.controller.api.response.UserCreateResponse;
import com.study.withus.user.service.UserService;
import com.study.withus.user.domain.entity.User;
import com.study.withus.user.controller.api.request.UserCreateRequest;
import com.study.withus.util.security.SecurityUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserApiController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity signupApi(@RequestBody @Valid UserCreateRequest dto) {
        User user = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserCreateResponse(user.getUsername(), user.getLoginId(), user.getUuid()));
    }

    @PutMapping
    public ResponseEntity modifyUserInfoApi(@RequestBody @Valid UserNormalInfoModifyRequest dto, @AuthenticationPrincipal SecurityUser auth) {
        User user = userService.modify(dto, auth.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(user.getUsername());
    }

    @GetMapping("/test")
    public ResponseEntity testSession(@AuthenticationPrincipal SecurityUser user) {
        return ResponseEntity.ok("테스트 : " + user.getUsername());
    }
}
