package com.pokemon.pokeapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.pokeapi.dto.auth.AuthResponseDTO;
import com.pokemon.pokeapi.dto.auth.LoginRequestDTO;
import com.pokemon.pokeapi.dto.auth.RegisterRequestDTO;
import com.pokemon.pokeapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private com.pokemon.pokeapi.security.JwtAuthFilter jwtAuthFilter;
    @MockBean
    private com.pokemon.pokeapi.security.JwtUtils jwtUtils;
    @MockBean
    private com.pokemon.pokeapi.security.CustomUserDetailsService customUserDetailsService;

    @Test
    void testRegister_Returns201() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO("user", "user@u.com", "password");
        when(userService.register(any())).thenReturn(new AuthResponseDTO("t", "u", "r"));
        
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isCreated());
    }
    
    @Test
    void testRegister_InvalidInput_Returns400() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO("", "invalid", "123");
        
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_Returns200() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO("user", "password");
        when(userService.login(any())).thenReturn(new AuthResponseDTO("t", "u", "r"));
        
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk());
    }
}
