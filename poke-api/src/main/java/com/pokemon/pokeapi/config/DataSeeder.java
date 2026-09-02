package com.pokemon.pokeapi.config;

import com.pokemon.pokeapi.dto.auth.RegisterRequestDTO;
import com.pokemon.pokeapi.repository.UserRepository;
import com.pokemon.pokeapi.service.LocalPokemonService;
import com.pokemon.pokeapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.LongStream;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserService userService;
    private final LocalPokemonService localPokemonService;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Seeding admin user...");
            userService.register(new RegisterRequestDTO("admin", "admin@pokemon.com", "admin123"));
            
            log.info("Seeding first 20 pokemon to local DB...");
            List<Long> ids = LongStream.rangeClosed(1, 20).boxed().collect(Collectors.toList());
            try {
                localPokemonService.syncPokemonBatch(ids);
                log.info("Finished seeding pokemon.");
            } catch (Exception e) {
                log.error("Failed to seed pokemon: " + e.getMessage());
            }
        }
    }
}
