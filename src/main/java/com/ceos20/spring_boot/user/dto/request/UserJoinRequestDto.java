package com.ceos20.spring_boot.user.dto.request;

import com.ceos20.spring_boot.user.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record UserJoinRequestDto(String name, String nickname, String password, String email, String phone) {

    public User toEntity(final BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .name(name)
                .nickname(nickname)
                .password(bCryptPasswordEncoder.encode(password))
                .email(email)
                .phone(phone)
                .build();
    }
}
