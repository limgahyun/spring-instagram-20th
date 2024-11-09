package com.ceos20.spring_boot.auth.service;

import com.ceos20.spring_boot.exception.ExceptionCode;
import com.ceos20.spring_boot.user.domain.User;
import com.ceos20.spring_boot.user.dto.request.UserJoinRequestDto;
import com.ceos20.spring_boot.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    public void joinUser(UserJoinRequestDto userDto) {
        checkEmailDuplication(userDto.email());
        checkNicknameDuplication(userDto.nickname());

        User user = userDto.toEntity(); //만들어진 객체를 user 엔티티로 변환
        userRepository.save(user); //user 엔티티 저장
    }

}