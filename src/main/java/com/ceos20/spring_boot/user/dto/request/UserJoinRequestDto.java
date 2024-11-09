package com.ceos20.spring_boot.user.dto.request;

import com.ceos20.spring_boot.user.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record UserJoinRequestDto(String name, String nickname, String password, String email, String phone) {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User toEntity() {
        return User.builder()
                .name(name)
                .nickname(nickname)
                .password(encoder.encode(password))
                .email(email)
                .phone(phone)
                .build();
    }
}
