package com.study.withus.util.security.hanlder;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String,String> data = new HashMap<>();
        data.put("loginId", authentication.getName());
        SuccessResponse successResponse = new SuccessResponse(data, response);
        objectMapper.writeValue(response.getWriter(), successResponse);
    }

    @Data
    static class SuccessResponse {
        private Map<String, String> data;

        SuccessResponse(Map<String, String> data, HttpServletResponse response ) {
            this.data = data;
            setHttpServletResponse(response);
        }

        private void setHttpServletResponse(HttpServletResponse response) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
        }

    }
}
