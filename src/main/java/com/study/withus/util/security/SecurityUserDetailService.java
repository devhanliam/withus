package com.study.withus.util.security;

import com.study.withus.user.infra.entity.User;
import com.study.withus.user.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class SecurityUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("로그인 정보가 올바르지 않습니다"));
        return new SecurityUser(user.getLoginId(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRoles())));
    }
}
