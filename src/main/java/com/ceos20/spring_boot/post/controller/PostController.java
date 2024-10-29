package com.ceos20.spring_boot.post.controller;

import com.ceos20.spring_boot.post.dto.request.PostCreatRequestDto;
import com.ceos20.spring_boot.post.dto.response.PostListResponseDto;
import com.ceos20.spring_boot.post.dto.response.PostResponseDto;
import com.ceos20.spring_boot.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post controller", description = "게시글 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 작성", description = "게시글 작성")
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody final PostCreatRequestDto requestDto, final String username) {
        postService.createPost(requestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "전체 게시글 조회", description = "전체 게시글 조회")
    @GetMapping
    public ResponseEntity<List<PostListResponseDto>> getAllPosts() {
        final List<PostListResponseDto> posts = postService.getAllPosts();
        return ResponseEntity.ok().body(posts);
    }

    @Operation(summary = "게시글 조회", description = "id로 게시글 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable final Long postId) {
        final PostResponseDto post = postService.getPost(postId);
        return ResponseEntity.ok().body(post);
    }




}
