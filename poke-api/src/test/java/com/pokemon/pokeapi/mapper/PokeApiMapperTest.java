package com.pokemon.pokeapi.mapper;

import com.pokemon.pokeapi.dto.external.*;
import com.pokemon.pokeapi.dto.pokemon.EvolutionNodeDTO;
import com.pokemon.pokeapi.model.Pokemon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PokeApiMapperImpl.class)
class PokeApiMapperTest {

    @Autowired
    private PokeApiMapper mapper;

    @Test
    void testExtractEnglishDescription() {
        PokeApiSpeciesDTO.PokeApiFlavorTextDTO enText = new PokeApiSpeciesDTO.PokeApiFlavorTextDTO("Hello world\n", new PokeApiNamedResourceDTO("en", null));
        PokeApiSpeciesDTO.PokeApiFlavorTextDTO esText = new PokeApiSpeciesDTO.PokeApiFlavorTextDTO("Hola", new PokeApiNamedResourceDTO("es", null));
        PokeApiSpeciesDTO species = new PokeApiSpeciesDTO(List.of(esText, enText), null);
        
        assertEquals("Hello world ", species.getEnglishDescription());
    }

    @Test
    void testExtractIdFromUrl() {
        PokeApiNamedResourceDTO resource = new PokeApiNamedResourceDTO("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/");
        assertEquals(1L, resource.extractId());
    }

    @Test
    void testToEntity() {
        PokeApiPokemonDTO.PokeApiSpritesDTO sprites = new PokeApiPokemonDTO.PokeApiSpritesDTO("url");
        PokeApiPokemonDTO data = new PokeApiPokemonDTO(1L, "bulba", 69, 7, 64, sprites, List.of(), List.of(), List.of());
        
        Pokemon pokemon = mapper.toEntity(data, "A strange seed.");
        
        assertNotNull(pokemon);
        assertEquals(1L, pokemon.getId());
        assertEquals("bulba", pokemon.getName());
        assertEquals("url", pokemon.getSpriteUrl());
        assertEquals("A strange seed.", pokemon.getDescription());
    }
}
