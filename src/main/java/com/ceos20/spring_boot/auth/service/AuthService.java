package com.ceos20.spring_boot.auth.service;

import com.ceos20.spring_boot.exception.ExceptionCode;
import com.ceos20.spring_boot.user.domain.User;
import com.ceos20.spring_boot.user.dto.request.UserJoinRequestDto;
import com.ceos20.spring_boot.user.repository.UserRepository;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER.getMessage()));
        user.login(password, bCryptPasswordEncoder);
        return user;
    }

    public void login(String rawPassword, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.password.mathPassword(rawPassword, bCryptPasswordEncoder);
    }

    public void mathPassword(String rawPassword, BCryptPasswordEncoder bCryptPasswordEncoder) {
        if (!bCryptPasswordEncoder.matches(rawPassword.password, this.password)){
            throw new
        }
    }
}