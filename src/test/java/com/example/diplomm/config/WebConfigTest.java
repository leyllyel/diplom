package com.example.diplomm.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebConfig.class)
    public class WebConfigTest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        public void testCorsConfiguration() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/test")
                            .header("Origin", "http://localhost:8081"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:8081"));
        }
    }