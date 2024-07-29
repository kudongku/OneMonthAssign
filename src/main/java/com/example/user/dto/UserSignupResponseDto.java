package com.example.user.dto;

import java.util.Set;
import lombok.Getter;

@Getter
public class UserSignupResponseDto {

    private final String username;
    private final String nickname;
    private final Set<String> authorities;

    public UserSignupResponseDto(String username, String nickname, Set<String> authorities) {
        this.username = username;
        this.nickname = nickname;
        this.authorities = authorities;
    }

}
