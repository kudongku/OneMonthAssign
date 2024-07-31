package com.example.user.service;

import com.example.user.dto.UserSignupRequestDto;
import com.example.user.dto.UserSignupResponseDto;
import com.example.user.entity.AuthorityEnum;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import com.example.user.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
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

        return new UserSignupResponseDto(
            user.getUsername(),
            user.getNickname(),
            user.getAuthorities()
        );
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

    public void createRefreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        String authHeader = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(JwtUtil.BEARER_PREFIX)) {
            return;
        }

        String accessToken = authHeader.substring(7);
        Claims claims = jwtUtil.getUserInfoFromToken(accessToken);
        String username = claims.getSubject();

        if (username != null) {
            User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("유효하지 않은 아이디입니다.")
            );

            String refreshToken = jwtUtil.createRefreshToken(
                user.getAuthorities(),
                user.getUsername()
            );
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("accessToken", accessToken);
            tokenMap.put("refreshToken", refreshToken);

            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, refreshToken);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(response.getWriter(), tokenMap);

        }
    }
}
