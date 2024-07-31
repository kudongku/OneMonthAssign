package com.example.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.user.common.UserFixture;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest implements UserFixture {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("회원가입 요청 테스트")
    class signup {

        @Test
        void 성공() {
            // given
            // when & then
            assertDoesNotThrow(() -> userService.signup(TEST_SIGN_UP_REQUEST_DTO));
            verify(userRepository, times(1))
                .save(any(User.class));
        }

        @Test
        void 중복된_닉네임_실패() {
            // given
            given(userRepository.existsByNickname(TEST_USER1.getNickname()))
                .willReturn(true);
            // when & then
            assertThatThrownBy(() -> userService.signup(TEST_SIGN_UP_REQUEST_DTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("동일한 닉네임이 존재합니다.");
        }

        @Test
        void 중복된_아이_실패() {
            // given
            given(userRepository.existsByUsername(TEST_USER1.getUsername()))
                .willReturn(true);
            // when & then
            assertThatThrownBy(() -> userService.signup(TEST_SIGN_UP_REQUEST_DTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("동일한 아이디가 존재합니다.");
        }
    }
}
