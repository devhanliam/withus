package com.study.withus.util.security.hanlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String msg = exception.getMessage();
        if (exception instanceof BadCredentialsException) {
            msg = "가입되지않은 회원이거나 아이디와 비밀번호가 옳지않습니다";
        }

        log.info("로그인 실패 :  {} ", msg);
        ErrorResponse errorResponse = new ErrorResponse(msg, response);
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    @Data
    static class ErrorResponse {
        private String content;

        ErrorResponse(String content,HttpServletResponse response ) {
            this.content = content;
            setHttpServletResponse(response);
        }

        private void setHttpServletResponse(HttpServletResponse response) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
        }
    }
}
