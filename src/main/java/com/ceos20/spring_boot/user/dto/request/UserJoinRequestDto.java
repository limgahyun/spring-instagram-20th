package com.ceos20.spring_boot.user.dto.request;

import com.ceos20.spring_boot.user.domain.User;

public record UserJoinRequestDto(String name, String nickname, String password, String email, String phone) {

    public User toEntity(String encodedPassword) {
        return User.builder()
                .name(name)
                .nickname(nickname)
                .password(encodedPassword)
                .email(email)
                .phone(phone)
                .build();
    }
}
