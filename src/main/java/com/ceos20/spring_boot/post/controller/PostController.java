package com.ceos20.spring_boot.post.controller;

import com.ceos20.spring_boot.post.dto.request.PostCreatRequestDto;
import com.ceos20.spring_boot.post.dto.response.PostListResponseDto;
import com.ceos20.spring_boot.post.dto.response.PostResponseDto;
import com.ceos20.spring_boot.post.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Repository
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody final PostCreatRequestDto requestDto, final String username) {
        postService.createPost(requestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<PostListResponseDto>> getAllPosts() {
        final List<PostListResponseDto> posts = postService.getAllPosts();
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable final Long postId) {
        final PostResponseDto post = postService.getPost(postId);
        return ResponseEntity.ok().body(post);
    }




}
