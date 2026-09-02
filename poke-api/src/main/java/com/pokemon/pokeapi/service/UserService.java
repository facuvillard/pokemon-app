package com.pokemon.pokeapi.service;

import com.pokemon.pokeapi.dto.auth.AuthResponseDTO;
import com.pokemon.pokeapi.dto.auth.LoginRequestDTO;
import com.pokemon.pokeapi.dto.auth.RegisterRequestDTO;
import com.pokemon.pokeapi.exception.BadRequestException;
import com.pokemon.pokeapi.model.User;
import com.pokemon.pokeapi.repository.UserRepository;
import com.pokemon.pokeapi.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .build();
        
        userRepository.save(user);
        
        String token = jwtUtils.generateToken(user.getUsername());
        return new AuthResponseDTO(token, user.getUsername(), user.getRole());
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
            );
            
            String token = jwtUtils.generateToken(dto.username());
            User user = userRepository.findByUsername(dto.username()).get();
            return new AuthResponseDTO(token, user.getUsername(), user.getRole());
        } catch (Exception e) {
            throw new BadRequestException("Invalid credentials");
        }
    }

    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }
}
