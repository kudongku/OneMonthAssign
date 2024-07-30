package com.example.user.filter;

import com.example.user.dto.UserLoginRequestDto;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import com.example.user.security.CustomAuthenticationToken;
import com.example.user.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationFilter(
        JwtUtil jwtUtil,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        setFilterProcessesUrl("/v1/users/login");
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException {
        try {
            UserLoginRequestDto requestDto = new ObjectMapper().readValue(
                request.getInputStream(),
                UserLoginRequestDto.class
            );

            User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
                () -> new RuntimeException("잘못된 아이디를 입력했습니다.")
            );

            if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                throw new RuntimeException("잘못된 비밀번호를 입력했습니다.");
            }

            return new CustomAuthenticationToken(
                user,
                requestDto.getPassword()
            );

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult
    ) throws IOException {
        User user = (User) authResult.getPrincipal();
        String token = jwtUtil.createToken(user.getId(), user.getUsername());
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), tokenMap);
    }

    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException failed
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("message", failed.getMessage());
        String newResponse = new ObjectMapper().writeValueAsString(json);
        response.setContentType("application/json");
        response.setContentLength(newResponse.getBytes(StandardCharsets.UTF_8).length);
        response.getOutputStream().write(newResponse.getBytes(StandardCharsets.UTF_8));
        log.error(failed.getMessage());
    }

}
