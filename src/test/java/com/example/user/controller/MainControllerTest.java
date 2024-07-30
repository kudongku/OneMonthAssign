package com.example.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(MainController.class)
public class MainControllerTest extends ControllerTest {

    @Test
    void 헬스체크_테스트() throws Exception {
        // given & when
        mockMvc.perform(get("/v1/health"))
            .andExpect(status().is2xxSuccessful())
            .andDo(print());
    }

}
