package com.pokemon.pokeapi.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokeApiEvolutionChainDataDTO(PokeApiChainLinkDTO chain) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiChainLinkDTO(
        PokeApiNamedResourceDTO species,
        @JsonProperty("evolution_details") List<PokeApiEvolutionDetailDTO> evolutionDetails,
        @JsonProperty("evolves_to") List<PokeApiChainLinkDTO> evolvesTo
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiEvolutionDetailDTO(@JsonProperty("min_level") Integer minLevel) {}
}
