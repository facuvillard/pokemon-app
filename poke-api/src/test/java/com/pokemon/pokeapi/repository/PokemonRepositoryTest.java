package com.pokemon.pokeapi.repository;

import com.pokemon.pokeapi.model.Pokemon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PokemonRepositoryTest {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    void saveAndFindPokemon() {
        Pokemon p1 = Pokemon.builder()
                .id(1L).name("Bulbasaur").baseExperience(64).height(7).weight(69)
                .build();
        pokemonRepository.save(p1);

        Pokemon result = pokemonRepository.findById(1L).orElse(null);

        assertNotNull(result);
        assertEquals("Bulbasaur", result.getName());
    }
}
