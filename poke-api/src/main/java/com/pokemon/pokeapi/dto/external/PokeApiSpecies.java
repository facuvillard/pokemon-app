package com.pokemon.pokeapi.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokeApiSpecies(
    @JsonProperty("flavor_text_entries") List<PokeApiFlavorText> flavorTextEntries,
    @JsonProperty("evolution_chain") PokeApiEvolutionChainRef evolutionChain
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiFlavorText(
        @JsonProperty("flavor_text") String flavorText,
        PokeApiNamedResource language
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiEvolutionChainRef(String url) {}
}
