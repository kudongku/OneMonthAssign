package com.example.user.controller;

import com.example.user.dto.UserLoginRequestDto;
import com.example.user.dto.UserSignupRequestDto;
import com.example.user.dto.UserSignupResponseDto;
import com.example.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "UserController", description = "회원가입을 포함한 컨트롤러입니다.")
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입 후 UserDTO를 Json타입으로 리턴합니다.")
    @PostMapping("/signup")
    public UserSignupResponseDto signup(@RequestBody UserSignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @Operation(summary = "로그인", description = "사용자 인증 후 JWT 토큰을 반환합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto loginRequest) {
        return ResponseEntity.ok("로그인 요청이 필터에서 처리됩니다.");
    }

    @Operation(summary = "refreshToken", description = "토큰 만료기한을 연장합니다.")
    @PostMapping("/refreshToken")
    public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        userService.createRefreshToken(request, response);
    }

}
