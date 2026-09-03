package com.pokemon.pokeapi.client;

import com.pokemon.pokeapi.dto.external.PokeApiEvolutionChainDataDTO;
import com.pokemon.pokeapi.dto.external.PokeApiListResponseDTO;
import com.pokemon.pokeapi.dto.external.PokeApiPokemonDTO;
import com.pokemon.pokeapi.dto.external.PokeApiSpeciesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PokeApiClient {

    private final RestTemplate restTemplate;

    @Value("${pokeapi.base-url}")
    private String baseUrl;

    @Cacheable("pokemonList")
    public PokeApiListResponseDTO getPokemonList(int offset, int limit) {
        String url = baseUrl + "/pokemon?offset=" + offset + "&limit=" + limit;
        return restTemplate.getForObject(url, PokeApiListResponseDTO.class);
    }

    @Cacheable("pokemonDetail")
    public PokeApiPokemonDTO getPokemonById(long id) {
        String url = baseUrl + "/pokemon/" + id;
        return restTemplate.getForObject(url, PokeApiPokemonDTO.class);
    }

    @Cacheable("pokemonSpecies")
    public PokeApiSpeciesDTO getPokemonSpecies(long id) {
        String url = baseUrl + "/pokemon-species/" + id;
        return restTemplate.getForObject(url, PokeApiSpeciesDTO.class);
    }

    @Cacheable("evolutionChain")
    public PokeApiEvolutionChainDataDTO getEvolutionChain(String url) {
        return restTemplate.getForObject(url, PokeApiEvolutionChainDataDTO.class);
    }
}
