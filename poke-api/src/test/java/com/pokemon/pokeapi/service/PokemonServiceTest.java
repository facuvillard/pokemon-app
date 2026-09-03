package com.pokemon.pokeapi.service;

import com.pokemon.pokeapi.client.PokeApiClient;
import com.pokemon.pokeapi.dto.external.PokeApiListResponseDTO;
import com.pokemon.pokeapi.dto.external.PokeApiNamedResourceDTO;
import com.pokemon.pokeapi.dto.external.PokeApiPokemonDTO;
import com.pokemon.pokeapi.dto.external.PokeApiSpeciesDTO;
import com.pokemon.pokeapi.dto.pokemon.PokemonDetailDTO;
import com.pokemon.pokeapi.dto.pokemon.PokemonListResponseDTO;
import com.pokemon.pokeapi.exception.ResourceNotFoundException;
import com.pokemon.pokeapi.mapper.PokeApiMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTest {

    @Mock
    private PokeApiClient pokeApiClient;

    @Mock
    private PokeApiMapper pokeApiMapper;

    @InjectMocks
    private PokemonService pokemonService;

    @Test
    void testListPokemon_ReturnsPagedResponse() {
        PokeApiListResponseDTO listResp = new PokeApiListResponseDTO(
            1, null, null, List.of(new PokeApiNamedResourceDTO("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"))
        );
        PokeApiPokemonDTO detail = new PokeApiPokemonDTO(1L, "bulbasaur", 69, null, null, null, null, null, null);

        when(pokeApiClient.getPokemonList(anyInt(), anyInt())).thenReturn(listResp);
        when(pokeApiClient.getPokemonById(1L)).thenReturn(detail);

        PokemonListResponseDTO result = pokemonService.listPokemon(0, 20);

        assertEquals(1, result.totalElements());
        assertEquals(1, result.content().size());
        assertEquals("bulbasaur", result.content().get(0).name());
    }

    @Test
    void testGetPokemonDetail_ReturnsCombinedData() {
        PokeApiPokemonDTO data = new PokeApiPokemonDTO(1L, "bulbasaur", 69, 7, null, null, null, null, null);
        PokeApiSpeciesDTO species = new PokeApiSpeciesDTO(
            List.of(new PokeApiSpeciesDTO.PokeApiFlavorTextDTO("A strange seed was planted.", new PokeApiNamedResourceDTO("en", null))),
            null
        );

        when(pokeApiClient.getPokemonById(1L)).thenReturn(data);
        when(pokeApiClient.getPokemonSpecies(1L)).thenReturn(species);

        PokemonDetailDTO result = pokemonService.getPokemonDetail(1L);

        assertEquals(1L, result.id());
        assertEquals("bulbasaur", result.name());
        assertEquals("A strange seed was planted.", result.description());
    }

    @Test
    void testGetPokemonDetail_WhenNotFound_ThrowsException() {
        when(pokeApiClient.getPokemonById(999L)).thenThrow(new RuntimeException());
        assertThrows(ResourceNotFoundException.class, () -> pokemonService.getPokemonDetail(999L));
    }
}
