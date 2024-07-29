package com.example.user.service;

import com.example.user.dto.UserSignupRequestDto;
import com.example.user.dto.UserSignupResponseDto;
import com.example.user.entity.AuthorityEnum;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserSignupResponseDto signup(UserSignupRequestDto requestDto) {
        validateUserName(requestDto.getUsername());
        validateNickname(requestDto.getNickname());
        String password = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(
            requestDto.getUsername(),
            requestDto.getNickname(),
            password,
            AuthorityEnum.USER.getAuthorityName()
        );

        userRepository.save(user);

        return new UserSignupResponseDto(user.getUsername(), user.getNickname(), user.getAuthorities());
    }

    private void validateNickname(String nickname) {

        if (userRepository.existsByNickname(nickname)) {
            throw new RuntimeException("동일한 닉네임이 존재합니다.");
        }

    }

    private void validateUserName(String username) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("동일한 아이디가 존재합니다.");
        }

    }

}
