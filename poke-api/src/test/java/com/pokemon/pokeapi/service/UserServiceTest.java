package com.pokemon.pokeapi.service;

import com.pokemon.pokeapi.dto.auth.AuthResponseDTO;
import com.pokemon.pokeapi.dto.auth.LoginRequestDTO;
import com.pokemon.pokeapi.dto.auth.RegisterRequestDTO;
import com.pokemon.pokeapi.exception.BadRequestException;
import com.pokemon.pokeapi.model.User;
import com.pokemon.pokeapi.repository.UserRepository;
import com.pokemon.pokeapi.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegister_Success() {
        RegisterRequestDTO dto = new RegisterRequestDTO("user", "u@u.com", "pass");
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("u@u.com")).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(jwtUtils.generateToken("user")).thenReturn("token");

        AuthResponseDTO result = userService.register(dto);
        assertEquals("token", result.token());
        assertEquals("user", result.username());
    }

    @Test
    void testRegister_DuplicateUsername_ThrowsBadRequest() {
        when(userRepository.existsByUsername("user")).thenReturn(true);
        assertThrows(BadRequestException.class, () -> userService.register(new RegisterRequestDTO("user", "u@u.com", "pass")));
    }
    
    @Test
    void testRegister_DuplicateEmail_ThrowsBadRequest() {
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("u@u.com")).thenReturn(true);
        assertThrows(BadRequestException.class, () -> userService.register(new RegisterRequestDTO("user", "u@u.com", "pass")));
    }

    @Test
    void testLogin_Success() {
        User u = new User();
        u.setUsername("user");
        u.setRole("USER");
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(jwtUtils.generateToken("user")).thenReturn("token");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(u));

        AuthResponseDTO result = userService.login(new LoginRequestDTO("user", "pass"));
        assertEquals("token", result.token());
    }

    @Test
    void testLogin_InvalidCredentials_ThrowsException() {
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException());
        assertThrows(BadRequestException.class, () -> userService.login(new LoginRequestDTO("user", "wrong")));
    }
}
