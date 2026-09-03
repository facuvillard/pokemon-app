package com.pokemon.pokeapi.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokeApiListResponse(
    Integer count,
    String next,
    String previous,
    List<PokeApiNamedResource> results
) {}
