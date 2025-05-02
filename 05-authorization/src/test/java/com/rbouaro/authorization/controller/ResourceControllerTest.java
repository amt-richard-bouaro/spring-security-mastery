package com.rbouaro.authorization.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@WebMvcTest(ResourceController.class)
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    void testHelloEndpointWithUserRole() throws Exception {
        mockMvc.perform(get("/api/v1/resources/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from Resource Controller!"));
    }

    @Test
    void testGoodbyeEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/resources/goodbye"))
                .andExpect(status().isOk())
                .andExpect(content().string("Goodbye from Resource Controller!"));
    }

    @Test
    public void testHelloEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/resources/hello"))
                .andExpect(status().isUnauthorized());
    }
}