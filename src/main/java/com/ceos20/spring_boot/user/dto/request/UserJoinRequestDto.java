package com.ceos20.spring_boot.user.dto.request;

import com.ceos20.spring_boot.user.domain.User;

public record UserJoinRequestDto(String name, String nickname, String password, String email, String phone) {

    public User toEntity() {
        return User.builder()
                .name(name)
                .nickname(nickname)
                .password(password)
                .email(email)
                .phone(phone)
                .build();
    }
}
