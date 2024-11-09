package com.ceos20.spring_boot.auth.controller;

import com.ceos20.spring_boot.auth.service.AuthService;
import com.ceos20.spring_boot.user.dto.request.UserJoinRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/join")
    public ApiResponse<String> join(@Valid @RequestBody UserJoinRequestDto request) {
        try {
            authService.createUser(request.getLoginId(), request.getLoginPw());
        } catch (Exception e) {
            return ApiResponse.of(e.getMessage());
        }
        return ApiResponse.ok("가입완료");
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getLoginPw())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ApiResponse.ok("로그인 성공");
        } catch (AuthenticationException e) {
            return ApiResponse.of("계정 정보를 확인하세요");
        }
    }
}