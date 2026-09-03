package com.pokemon.pokeapi.client;

import com.pokemon.pokeapi.dto.external.PokeApiEvolutionChainDataDTO;
import com.pokemon.pokeapi.dto.external.PokeApiListResponseDTO;
import com.pokemon.pokeapi.dto.external.PokeApiPokemonDTO;
import com.pokemon.pokeapi.dto.external.PokeApiSpeciesDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokeApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PokeApiClient pokeApiClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(pokeApiClient, "baseUrl", "https://pokeapi.co/api/v2");
    }

    @Test
    void getPokemonList_ReturnsData() {
        PokeApiListResponseDTO mockResponse = new PokeApiListResponseDTO(10, "next", "prev", null);
        when(restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon?offset=0&limit=10", PokeApiListResponseDTO.class))
                .thenReturn(mockResponse);

        PokeApiListResponseDTO response = pokeApiClient.getPokemonList(0, 10);
        assertNotNull(response);
        assertEquals(10, response.count());
    }

    @Test
    void getPokemonById_ReturnsData() {
        PokeApiPokemonDTO mockResponse = new PokeApiPokemonDTO(1L, "bulbasaur", 69, 7, 64, null, null, null, null);
        when(restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon/1", PokeApiPokemonDTO.class))
                .thenReturn(mockResponse);

        PokeApiPokemonDTO response = pokeApiClient.getPokemonById(1);
        assertNotNull(response);
        assertEquals("bulbasaur", response.name());
    }

    @Test
    void getPokemonSpecies_ReturnsData() {
        PokeApiSpeciesDTO mockResponse = new PokeApiSpeciesDTO(null, null);
        when(restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon-species/1", PokeApiSpeciesDTO.class))
                .thenReturn(mockResponse);

        PokeApiSpeciesDTO response = pokeApiClient.getPokemonSpecies(1);
        assertNotNull(response);
    }

    @Test
    void getEvolutionChain_ReturnsData() {
        PokeApiEvolutionChainDataDTO mockResponse = new PokeApiEvolutionChainDataDTO(null);
        String url = "https://pokeapi.co/api/v2/evolution-chain/1";
        when(restTemplate.getForObject(url, PokeApiEvolutionChainDataDTO.class))
                .thenReturn(mockResponse);

        PokeApiEvolutionChainDataDTO response = pokeApiClient.getEvolutionChain(url);
        assertNotNull(response);
    }
}
