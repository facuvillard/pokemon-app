package com.pokemon.pokeapi.service;

import com.pokemon.pokeapi.dto.pokemon.PokemonDetailDTO;
import com.pokemon.pokeapi.dto.pokemon.PokemonListResponseDTO;
import com.pokemon.pokeapi.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTest {

    @Mock
    private PokeApiService pokeApiService;

    @InjectMocks
    private PokemonService pokemonService;

    @Test
    void testListPokemon_ReturnsPagedResponse() {
        Map<String, Object> listResp = Map.of(
            "count", 1,
            "results", List.of(Map.of("url", "https://pokeapi.co/api/v2/pokemon/1/"))
        );
        Map<String, Object> detail = Map.of("id", 1, "name", "bulbasaur", "weight", 69);

        when(pokeApiService.getPokemonList(anyInt(), anyInt())).thenReturn(listResp);
        when(pokeApiService.getPokemonById(1L)).thenReturn(detail);

        PokemonListResponseDTO result = pokemonService.listPokemon(0, 20);

        assertEquals(1, result.totalElements());
        assertEquals(1, result.content().size());
        assertEquals("bulbasaur", result.content().get(0).name());
    }

    @Test
    void testGetPokemonDetail_ReturnsCombinedData() {
        Map<String, Object> data = Map.of("id", 1, "name", "bulbasaur", "weight", 69, "height", 7);
        Map<String, Object> species = Map.of(
            "flavor_text_entries", List.of(Map.of("language", Map.of("name", "en"), "flavor_text", "A strange seed was planted."))
        );

        when(pokeApiService.getPokemonById(1L)).thenReturn(data);
        when(pokeApiService.getPokemonSpecies(1L)).thenReturn(species);

        PokemonDetailDTO result = pokemonService.getPokemonDetail(1L);

        assertEquals(1L, result.id());
        assertEquals("bulbasaur", result.name());
        assertEquals("A strange seed was planted.", result.description());
    }

    @Test
    void testGetPokemonDetail_WhenNotFound_ThrowsException() {
        when(pokeApiService.getPokemonById(999L)).thenThrow(new RuntimeException());
        assertThrows(ResourceNotFoundException.class, () -> pokemonService.getPokemonDetail(999L));
    }
}
