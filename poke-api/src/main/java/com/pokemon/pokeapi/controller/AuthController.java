package com.pokemon.pokeapi.controller;

import com.pokemon.pokeapi.dto.auth.AuthResponseDTO;
import com.pokemon.pokeapi.dto.auth.LoginRequestDTO;
import com.pokemon.pokeapi.dto.auth.RegisterRequestDTO;
import com.pokemon.pokeapi.model.User;
import com.pokemon.pokeapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO register(@Valid @RequestBody RegisterRequestDTO dto) {
        return userService.register(dto);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        return userService.login(dto);
    }

    @GetMapping("/me")
    public User getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getCurrentUser(userDetails.getUsername());
    }
}
