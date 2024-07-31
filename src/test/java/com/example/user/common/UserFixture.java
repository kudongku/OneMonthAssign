package com.example.user.common;


import com.example.user.dto.UserLoginRequestDto;
import com.example.user.dto.UserSignupRequestDto;
import com.example.user.entity.User;
import java.util.Set;

public interface UserFixture {
    Long TEST_USER1_ID = 1L;
    String TEST_USER_NICKNAME = "testUserNickname";
    String TEST_USER_USERNAME = "testUserUsername";
    String TEST_USER_PASSWORD = "testPassword";
    Set<String> TEST_USER_AUTHORITIES = Set.of("ROLE_USER");

    User TEST_USER1 = new User(TEST_USER1_ID, TEST_USER_USERNAME, TEST_USER_NICKNAME, TEST_USER_PASSWORD, TEST_USER_AUTHORITIES);
    UserSignupRequestDto TEST_SIGN_UP_REQUEST_DTO = new UserSignupRequestDto(TEST_USER_USERNAME, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
    UserLoginRequestDto TEST_LOG_IN_REQUEST_DTO = new UserLoginRequestDto(TEST_USER_USERNAME, TEST_USER_PASSWORD);
}
