package com.example.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

@WebMvcTest(UserController.class)
public class UserControllerTest extends ControllerTest {

    @Test
    void 회원가입_테스트() throws Exception {
        // given & when
        mockMvc.perform(post("/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_SIGN_UP_REQUEST_DTO))
            )
            .andExpect(status().is2xxSuccessful())
            .andDo(print());
    }

    @Test
    void 로그인_테스트() throws Exception {
        // given & when
        mockMvc.perform(post("/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_LOG_IN_REQUEST_DTO))
            )
            .andExpect(status().is2xxSuccessful())
            .andDo(print());
    }

}
