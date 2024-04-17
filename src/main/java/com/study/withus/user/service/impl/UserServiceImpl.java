package com.study.withus.user.service.impl;

import com.study.withus.user.controller.api.request.UserNormalInfoModifyRequest;
import com.study.withus.user.domain.entity.User;
import com.study.withus.user.domain.repository.UserRepository;
import com.study.withus.user.controller.api.request.UserCreateRequest;
import com.study.withus.user.service.UserService;
import com.study.withus.util.exceptionhandler.exception.UserDuplicatedException;
import com.study.withus.util.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User create(UserCreateRequest dto) {
        userRepository.findByLoginId(dto.getLoginId()).ifPresent(a -> {
                    throw new UserDuplicatedException();
        });
        User user = User.create(dto, passwordEncoder);
       return userRepository.save(user);
    }

    @Transactional
    @Override
    public User modify(UserNormalInfoModifyRequest dto, String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("세션이 만료되었거나 존재하지 않는 회원입니다."));
        user.modifyNormalInfo(dto);
        return userRepository.save(user);
    }
}
