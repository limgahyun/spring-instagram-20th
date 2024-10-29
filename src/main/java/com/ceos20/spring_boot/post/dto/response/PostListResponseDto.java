package com.ceos20.spring_boot.post.dto.response;

import com.ceos20.spring_boot.post.domain.Post;

public record PostListResponseDto(String content, String username) {
    public static PostListResponseDto from(final Post post) {
        return new PostListResponseDto(post.getContent(), post.getWriterNickname());
    }
}
