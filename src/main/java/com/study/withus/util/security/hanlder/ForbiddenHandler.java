package com.study.withus.util.security.hanlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class ForbiddenHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LoginAuthenticationFailureHandler.ErrorResponse errorResponse = new LoginAuthenticationFailureHandler.ErrorResponse(accessDeniedException.getMessage(), response);
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
