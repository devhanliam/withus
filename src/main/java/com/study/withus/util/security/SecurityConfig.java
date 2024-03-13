package com.study.withus.util.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.withus.user.infra.repository.UserRepository;
import com.study.withus.util.security.hanlder.ForbiddenHandler;
import com.study.withus.util.security.hanlder.LoginAuthenticationFailureHandler;
import com.study.withus.util.security.filter.LoginAuthenticationFilter;
import com.study.withus.util.security.hanlder.LoginAuthenticationSuccessHandler;
import com.study.withus.util.security.hanlder.UnauthorizedHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.bcel.BcelGenericSignatureToTypeXConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
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
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RedisIndexedSessionRepository redisSessionRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable());
        http
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());
        http.sessionManagement(session -> session
                .sessionConcurrency(concurrency -> concurrency
                        .sessionRegistry(sessionRegistry())
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .expiredSessionStrategy(event -> {
                            HttpServletResponse response = event.getResponse();
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            objectMapper.writeValue(response.getWriter(),"세션이 만료되었거나 다른곳에서 로그인 되었습니다");
                        })
                )
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        http.authorizeHttpRequests(req -> req.requestMatchers("/main","/login","/signup","/api/v1/signup","/favicon.*").permitAll()
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
        http.with(LoginFilterConfigurer.loginFilterConfigurer(), dsl -> dsl
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .authenticationManager(authenticationManager())
                .objectMapper(objectMapper));
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
        return new SecurityUserDetailService(userRepository);
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
