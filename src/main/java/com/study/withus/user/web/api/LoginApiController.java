package com.study.withus.user.web.api;

import com.study.withus.user.app.SignupService;
import com.study.withus.user.web.dto.request.SignupRequestDto;
import com.study.withus.util.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LoginApiController {
    private final SignupService signupService;

    @PostMapping("/api/v1/login")
    public ResponseEntity loginApiController() {
        return ResponseEntity.ok("로그인 완료");
    }

    @PostMapping(value = "api/v1/signup")
    public ResponseEntity signupApiController(@RequestBody @Valid SignupRequestDto dto, HttpServletRequest request) {
        signupService.signup(dto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @GetMapping("/api/v1/user/test")
    public ResponseEntity testSession(@AuthenticationPrincipal SecurityUser user) {
        return ResponseEntity.ok("테스트 : " + user.getUsername());
    }
}
