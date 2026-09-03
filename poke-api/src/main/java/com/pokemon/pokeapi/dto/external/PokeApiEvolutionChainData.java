package com.pokemon.pokeapi.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokeApiEvolutionChainData(PokeApiChainLink chain) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiChainLink(
        PokeApiNamedResource species,
        @JsonProperty("evolution_details") List<PokeApiEvolutionDetail> evolutionDetails,
        @JsonProperty("evolves_to") List<PokeApiChainLink> evolvesTo
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiEvolutionDetail(@JsonProperty("min_level") Integer minLevel) {}
}
