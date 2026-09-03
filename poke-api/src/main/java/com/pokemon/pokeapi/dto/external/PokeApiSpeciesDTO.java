package com.pokemon.pokeapi.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokeApiSpeciesDTO(
    @JsonProperty("flavor_text_entries") List<PokeApiFlavorTextDTO> flavorTextEntries,
    @JsonProperty("evolution_chain") PokeApiEvolutionChainRefDTO evolutionChain
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiFlavorTextDTO(
        @JsonProperty("flavor_text") String flavorText,
        PokeApiNamedResourceDTO language
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiEvolutionChainRefDTO(String url) {}
}
