package com.example.basic_crm.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_correctCredentials_shouldReturnStatusOk() throws Exception {

        mockMvc.perform(post("/login")
                .content("{\"username\": \"user\", \"password\": \"user\"}")
        )
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    void login_correctCredentials_shouldReturnJWTInResponseHeader() throws Exception {

        MvcResult result = mockMvc.perform(post("/login")
                .content("{\"username\": \"user\", \"password\": \"user\"}")
        )
                .andDo(print())
                .andReturn();

        String token = result.getResponse().getHeader("Authorization");

        assertEquals("Bearer ", token.substring(0, 7));
    }

    @Test
    void login_incorrectCredentials_shouldReturnStatusUnauthorized() throws Exception {

        mockMvc.perform(post("/login")
                .content("{\"username\": \"bad_user\", \"password\": \"bad_user\"}")
        )
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    void secured_shouldLoginAndGetContent() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .content("{\"username\": \"user\", \"password\": \"user\"}")
        )
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        String token = result.getResponse().getHeader("Authorization");

        mockMvc.perform(get("/secured")
                .header("Authorization", token)
        )
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().string("secured"));
    }

    @Test
    void secured_noJWT_shouldReturnStatusUnauthorized() throws Exception {

        mockMvc.perform(get("/secured"))
                .andDo(print())
                .andExpect(status().is(401));
    }
}
