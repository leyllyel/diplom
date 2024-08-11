package com.example.diplomm.adapter;

import com.example.diplomm.repository.UserRepository;
import com.example.diplomm.users.FileInfo;
import com.example.diplomm.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityConfig.class)
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testLoginSuccess() throws Exception {
        User user = new User("testUser", "testPassword");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Отправляем запрос на логин
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "testUser")
                        .param("password", "testPassword"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginFailure() throws Exception {
        // Задаем mock для несуществующего пользователя
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        // Отправляем запрос на логин
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "testUser")
                        .param("password", "testPassword"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAuthenticatedResource() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/secured"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnAuthenticatedResource() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/secured"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/logout"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }
}