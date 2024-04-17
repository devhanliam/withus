package com.study.withus.user.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class UserViewController {

    @RequestMapping("/login")
    public String userLoginView() {
        return "/login";
    }

    @RequestMapping("/signup")
    public String userSignupView() {
        return "/signup";
    }

    @RequestMapping("/user/main")
    public String userMainView() {
        return "/main";
    }
}
