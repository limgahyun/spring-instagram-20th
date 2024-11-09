package com.ceos20.spring_boot.auth.controller;

import com.ceos20.spring_boot.auth.service.AuthService;
import com.ceos20.spring_boot.post.dto.request.PostCreatRequestDto;
import com.ceos20.spring_boot.post.service.PostService;
import com.ceos20.spring_boot.user.dto.request.UserJoinRequestDto;
import com.ceos20.spring_boot.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth controller", description = "")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입")
    public ResponseEntity<String> join(@Valid @RequestBody UserJoinRequestDto request) {
        userService.joinUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}