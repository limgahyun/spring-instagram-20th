package com.ceos20.spring_boot.user.dto.response;

import com.ceos20.spring_boot.user.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponseDto(
        String name,
        String nickname,
        String email,
        String phone
) {
    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    };
}