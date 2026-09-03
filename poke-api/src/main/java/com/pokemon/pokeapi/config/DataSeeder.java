package com.pokemon.pokeapi.config;

import com.pokemon.pokeapi.dto.auth.RegisterRequestDTO;
import com.pokemon.pokeapi.repository.UserRepository;
import com.pokemon.pokeapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Seeding demo admin user...");
            userService.register(new RegisterRequestDTO("admin", "admin@pokemon.com", "admin123"));
            log.info("Demo user created: admin / admin123");
        } else {
            log.info("Users already exist, skipping seed.");
        }
    }
}
