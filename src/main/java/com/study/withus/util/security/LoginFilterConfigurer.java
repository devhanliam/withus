package com.study.withus.util.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.withus.util.security.filter.LoginAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;

public class LoginFilterConfigurer extends AbstractHttpConfigurer<LoginFilterConfigurer, HttpSecurity> {
    private  AuthenticationSuccessHandler successHandler;
    private  AuthenticationFailureHandler failureHandler;
    private  AuthenticationManager authenticationManager;
    private ObjectMapper objectMapper;

    @Override
    public void init(HttpSecurity http) throws Exception {

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        LoginAuthenticationFilter authFilter = new LoginAuthenticationFilter("/login", objectMapper);
        authFilter.setAuthenticationManager(authenticationManager);
        authFilter.setAuthenticationSuccessHandler(successHandler);
        authFilter.setAuthenticationFailureHandler(failureHandler);
        authFilter.setSecurityContextRepository(http.getSharedObject(SecurityContextRepository.class));
        authFilter.setSessionAuthenticationStrategy(http.getSharedObject(SessionAuthenticationStrategy.class));
        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
    }

    public LoginFilterConfigurer successHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public LoginFilterConfigurer failureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }

    public LoginFilterConfigurer authenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        return this;
    }

    public LoginFilterConfigurer objectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public static LoginFilterConfigurer loginFilterConfigurer() {
        return new LoginFilterConfigurer();
    }
}
