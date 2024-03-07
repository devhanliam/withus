package com.study.withus.user.app;

import com.study.withus.user.infra.entity.User;
import com.study.withus.user.infra.repository.UserRepository;
import com.study.withus.user.web.dto.request.SignupRequestDto;
import com.study.withus.util.exceptionhandler.exception.UserDuplicatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SignupServiceImpl implements SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void signup(SignupRequestDto dto) {
        userRepository.findByLoginId(dto.getLoginId()).ifPresent(a -> {
                    throw new UserDuplicatedException();
        });
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .uuid(uuid)
                .isSearchable(true)
                .createdDate(now)
                .modifiedDate(now)
                .roles("ROLE_USER").build();
        userRepository.save(user);
    }
}
