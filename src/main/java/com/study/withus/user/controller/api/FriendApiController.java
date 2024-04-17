package com.study.withus.user.controller.api;

import com.study.withus.user.controller.api.response.FriendGetResponse;
import com.study.withus.user.controller.api.response.FriendshipCreateResponse;
import com.study.withus.user.service.FriendService;
import com.study.withus.util.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController("/api/v1/user/friend")
public class FriendApiController {

    private final FriendService friendService;

    @GetMapping("/list")
    public ResponseEntity getFriendList(@AuthenticationPrincipal SecurityUser auth) {
        List<FriendGetResponse> responses = friendService.getList(auth.getUsername());
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping("/friendship/{targetId}")
    public ResponseEntity requestFriendship(@PathVariable String targetId, @AuthenticationPrincipal SecurityUser auth) {
        FriendshipCreateResponse response = friendService.requestFriendship(targetId, auth.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/friendship/{targetId}")
    public ResponseEntity acceptFriendship(@PathVariable String targetId, @AuthenticationPrincipal SecurityUser auth) {
        friendService.acceptFriendship(targetId, auth.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

}
