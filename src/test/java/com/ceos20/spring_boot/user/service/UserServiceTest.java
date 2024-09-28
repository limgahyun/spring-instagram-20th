package com.ceos20.spring_boot.user.service;

import com.ceos20.spring_boot.user.domain.User;
import com.ceos20.spring_boot.user.dto.request.UserJoinRequestDto;
import com.ceos20.spring_boot.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    // nickname, email 모두 중복되지 않은 신규회원 가입 테스트
    @Test
    void joinNewUserTest() {
        //given
        final UserJoinRequestDto request = new UserJoinRequestDto("test", "nickname", "1234", "test@test.com", "01012345678");
        when(userRepository.existsByNickname(request.nickname())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);

        //when
        userService.joinUser(request);

        //then
        verify(userRepository).save(any(User.class));
    }

    // nickname이 중복인 경우 회원가입 테스트
    @Test
    void joinSameUserTest() {
        //given
        final UserJoinRequestDto request = new UserJoinRequestDto("test", "nickname", "1234", "test@test.com", "01012345678");
        when(userRepository.existsByNickname(request.nickname())).thenReturn(true); // exsitsByNickname = true 반환
        when(userRepository.existsByEmail(request.email())).thenReturn(false);

        //when
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> userService.joinUser(request)
        );

        //then
        System.out.println("Test Result: " + exception.getMessage());

        assertEquals("Nickname already exists", exception.getMessage());
        verify(userRepository).existsByNickname(request.nickname());
        verify(userRepository, never()).save(any(User.class));
    }

    // 회원 탈퇴 테스트
    @Test
    void deleteUserTest() {

        // given
        final User user = User.builder()
                .id(1L)
                .name("name")
                .nickname("nickname")
                .password("1234")
                .email("test@test.com")
                .phone("01012345678")
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        userService.deleteUser(user.getId());

        // then
        verify(userRepository).delete(any());

    }
}
