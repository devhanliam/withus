package com.study.withus.util.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.withus.user.domain.repository.UserRepository;
import com.study.withus.util.security.hanlder.ForbiddenHandler;
import com.study.withus.util.security.hanlder.LoginAuthenticationFailureHandler;
import com.study.withus.util.security.hanlder.LoginAuthenticationSuccessHandler;
import com.study.withus.util.security.hanlder.UnauthorizedHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userJpaRepository;
    private final ObjectMapper objectMapper;
    private final RedisIndexedSessionRepository redisSessionRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form
                    .loginPage("/login")
                    .usernameParameter("loginId")
                    .passwordParameter("password")
                    .successHandler(authenticationSuccessHandler())
                    .failureHandler(authenticationFailureHandler()));
        http.sessionManagement(session -> session
                .sessionConcurrency(concurrency -> concurrency
                        .sessionRegistry(sessionRegistry())
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredSessionStrategy(event -> {
                            HttpServletResponse response = event.getResponse();
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            objectMapper.writeValue(response.getWriter(),"세션이 만료되었거나 다른곳에서 로그인 되었습니다");
                        })
                )
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        http.authorizeHttpRequests(req -> req
                .requestMatchers(HttpMethod.POST,"/api/v1/user").permitAll()
                .requestMatchers("/","/user/main","/login","/signup","/8bitchar.png","/favicon.*").permitAll()
                .requestMatchers("/api/v1/user/**").hasRole("USER")
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());
        http.logout(logout -> logout.logoutUrl("/logout")
                .deleteCookies("SESSION")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler((request,response,authentication)->{}));
        http.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
//        redisSessionRepository.setCleanupCron(ScheduledTaskRegistrar.CRON_DISABLED);
        return new SpringSessionBackedSessionRegistry<>(redisSessionRepository);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new SecurityUserDetailService(userJpaRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new LoginAuthenticationFailureHandler(objectMapper);
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new LoginAuthenticationSuccessHandler(objectMapper);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ForbiddenHandler(objectMapper);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new UnauthorizedHandler(objectMapper);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }
}
