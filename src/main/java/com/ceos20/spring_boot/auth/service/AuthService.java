package com.ceos20.spring_boot.auth.service;

import com.ceos20.spring_boot.exception.ExceptionCode;
import com.ceos20.spring_boot.user.domain.User;
import com.ceos20.spring_boot.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService implements UserDetailsService {

    private final UserRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER.getMessage()));

    }
}