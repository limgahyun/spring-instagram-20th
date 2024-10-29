package com.ceos20.spring_boot.post.dto.response;

import com.ceos20.spring_boot.comment.domain.PostComment;
import com.ceos20.spring_boot.post.domain.Post;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostResponseDto(String content, String username, List<PostComment> commentList) {
    public static PostResponseDto of(final Post post, List<PostComment> commentList) {
        return new PostResponseDto(post.getContent(), post.getWriterNickname(), commentList);
    }
}
