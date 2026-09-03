package com.pokemon.pokeapi.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokeApiPokemon(
    Long id,
    String name,
    Integer weight,
    Integer height,
    @JsonProperty("base_experience") Integer baseExperience,
    PokeApiSprites sprites,
    List<PokeApiTypeSlot> types,
    List<PokeApiAbilitySlot> abilities,
    List<PokeApiStatSlot> stats
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiSprites(@JsonProperty("front_default") String frontDefault) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiTypeSlot(PokeApiNamedResource type) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiAbilitySlot(PokeApiNamedResource ability) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiStatSlot(@JsonProperty("base_stat") Integer baseStat, PokeApiNamedResource stat) {}
}
