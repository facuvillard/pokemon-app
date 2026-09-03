package com.pokemon.pokeapi.client;

import com.pokemon.pokeapi.dto.external.PokeApiEvolutionChainData;
import com.pokemon.pokeapi.dto.external.PokeApiListResponse;
import com.pokemon.pokeapi.dto.external.PokeApiPokemon;
import com.pokemon.pokeapi.dto.external.PokeApiSpecies;
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
    public PokeApiListResponse getPokemonList(int offset, int limit) {
        String url = baseUrl + "/pokemon?offset=" + offset + "&limit=" + limit;
        return restTemplate.getForObject(url, PokeApiListResponse.class);
    }

    @Cacheable("pokemonDetail")
    public PokeApiPokemon getPokemonById(long id) {
        String url = baseUrl + "/pokemon/" + id;
        return restTemplate.getForObject(url, PokeApiPokemon.class);
    }

    @Cacheable("pokemonSpecies")
    public PokeApiSpecies getPokemonSpecies(long id) {
        String url = baseUrl + "/pokemon-species/" + id;
        return restTemplate.getForObject(url, PokeApiSpecies.class);
    }

    @Cacheable("evolutionChain")
    public PokeApiEvolutionChainData getEvolutionChain(String url) {
        return restTemplate.getForObject(url, PokeApiEvolutionChainData.class);
    }
}
