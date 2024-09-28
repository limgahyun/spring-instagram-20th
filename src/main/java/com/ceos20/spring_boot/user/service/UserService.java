package com.ceos20.spring_boot.user.service;

import com.ceos20.spring_boot.user.domain.User;
import com.ceos20.spring_boot.user.dto.request.UserJoinRequestDto;
import com.ceos20.spring_boot.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public void checkNicknameDuplication(String nickname) {
        boolean nicknameDuplicate = userRepository.existsByNickname(nickname);
        if (nicknameDuplicate) {
            throw new IllegalStateException("Nickname already exists");
        }
    }

    @Transactional(readOnly = true)
    public void checkEmailDuplication(String email) {
        boolean nicknameDuplicate = userRepository.existsByEmail(email);
        if (nicknameDuplicate) {
            throw new IllegalStateException("Account already exists with this email");
        }
    }

    @Transactional
    public void joinUser(UserJoinRequestDto userDto) {
        checkEmailDuplication(userDto.email());
        checkNicknameDuplication(userDto.nickname());

        User user = userDto.toEntity();
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void deleteUser(Long id) {
        final User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }
}
