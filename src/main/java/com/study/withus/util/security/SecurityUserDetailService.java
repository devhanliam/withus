package com.study.withus.util.security;

import com.study.withus.user.domain.User;
import com.study.withus.user.infra.persistence.entity.UserEntity;
import com.study.withus.user.infra.persistence.repository.UserJpaRepository;
import com.study.withus.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class SecurityUserDetailService implements UserDetailsService {

    private final UserRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userJpaRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("로그인 정보가 올바르지 않습니다"));
        return new SecurityUser(userEntity.getLoginId(), userEntity.getPassword(), List.of(new SimpleGrantedAuthority(userEntity.getRoles())));
    }
}
