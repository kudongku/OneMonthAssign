package com.example.user.controller;

import com.example.user.dto.UserSignupRequestDto;
import com.example.user.dto.UserSignupResponseDto;
import com.example.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @Operation(summary = "업체 회원가입", description = "업체 측에서 회원가입 할 때 사용하는 API")
    @PostMapping("/signup")
    public UserSignupResponseDto signup(@RequestBody UserSignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }
}
