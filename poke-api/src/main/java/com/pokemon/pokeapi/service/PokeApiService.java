package com.pokemon.pokeapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PokeApiService {

    private final RestTemplate restTemplate;

    @Value("${pokeapi.base-url}")
    private String baseUrl;

    @Cacheable("pokemonList")
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPokemonList(int offset, int limit) {
        String url = baseUrl + "/pokemon?offset=" + offset + "&limit=" + limit;
        return restTemplate.getForObject(url, Map.class);
    }

    @Cacheable("pokemonDetail")
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPokemonById(long id) {
        String url = baseUrl + "/pokemon/" + id;
        return restTemplate.getForObject(url, Map.class);
    }

    @Cacheable("pokemonSpecies")
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPokemonSpecies(long id) {
        String url = baseUrl + "/pokemon-species/" + id;
        return restTemplate.getForObject(url, Map.class);
    }

    @Cacheable("evolutionChain")
    @SuppressWarnings("unchecked")
    public Map<String, Object> getEvolutionChain(String url) {
        return restTemplate.getForObject(url, Map.class);
    }
}
