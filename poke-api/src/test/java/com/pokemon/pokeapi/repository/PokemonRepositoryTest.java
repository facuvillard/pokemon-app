package com.pokemon.pokeapi.repository;

import com.pokemon.pokeapi.model.Pokemon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PokemonRepositoryTest {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    void searchByAllVisibleFields_ReturnsMatches() {
        Pokemon p1 = Pokemon.builder()
                .id(1L).name("Bulbasaur").baseExperience(64).height(7).weight(69)
                .build();
        Pokemon p2 = Pokemon.builder()
                .id(2L).name("Ivysaur").baseExperience(142).height(10).weight(130)
                .build();
        pokemonRepository.save(p1);
        pokemonRepository.save(p2);

        Page<Pokemon> result = pokemonRepository.searchByAllVisibleFields("saur", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void searchByAllVisibleFields_NoMatches_ReturnsEmpty() {
        Pokemon p1 = Pokemon.builder()
                .id(1L).name("Bulbasaur").baseExperience(64).height(7).weight(69)
                .build();
        pokemonRepository.save(p1);

        Page<Pokemon> result = pokemonRepository.searchByAllVisibleFields("mew", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }
}
