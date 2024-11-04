package com.ceos20.spring_boot.post.service;

import com.ceos20.spring_boot.comment.domain.PostComment;
import com.ceos20.spring_boot.comment.repository.CommentRepository;
import com.ceos20.spring_boot.config.exception.ExceptionCode;
import com.ceos20.spring_boot.post.domain.Post;
import com.ceos20.spring_boot.post.dto.request.PostCreatRequestDto;
import com.ceos20.spring_boot.post.dto.response.PostListResponseDto;
import com.ceos20.spring_boot.post.dto.response.PostResponseDto;
import com.ceos20.spring_boot.post.repository.PostRepository;
import com.ceos20.spring_boot.user.domain.User;
import com.ceos20.spring_boot.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional (readOnly = true)
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createPost (final PostCreatRequestDto request, final String username) {
        final User writer = userRepository.findByNickname(username);

        final Post post = Post.builder()
                .user(writer)
                .content(request.content())
                .build();
        postRepository.save(post);
    }

    public PostResponseDto getPost(final Long postId) {
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST.getMessage()));
        final List<PostComment> commentList = commentRepository.findByPost(post);

        return PostResponseDto.of(post, commentList);
    }

    public List<PostListResponseDto> getAllPosts() {
        final List<Post> postList = postRepository.findAll();
        return postList.stream()
                .map(PostListResponseDto::from)
                .collect(Collectors.toList());
    }

}
