package com.study.withus.user.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginViewController() {
        return "/login";
    }

    @GetMapping("/signup")
    public String signupViewController() {
        return "/signup";
    }
}
