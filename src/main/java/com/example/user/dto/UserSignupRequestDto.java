package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignupRequestDto {

    private String username;
    private String nickname;
    private String password;

}
