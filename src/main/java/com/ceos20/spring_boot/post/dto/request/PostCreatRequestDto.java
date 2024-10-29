package com.ceos20.spring_boot.post.dto.request;

import com.ceos20.spring_boot.post.domain.Post;
import com.ceos20.spring_boot.user.domain.User;

public record PostCreatRequestDto(User user, String content) {
    public Post toEntity() {
        return Post.builder()
                .user(user)
                .content(content)
                .build();
    }
}
