package com.pokemon.pokeapi.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokeApiPokemonDTO(
    Long id,
    String name,
    Integer weight,
    Integer height,
    @JsonProperty("base_experience") Integer baseExperience,
    PokeApiSpritesDTO sprites,
    List<PokeApiTypeSlotDTO> types,
    List<PokeApiAbilitySlotDTO> abilities,
    List<PokeApiStatSlotDTO> stats
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiSpritesDTO(@JsonProperty("front_default") String frontDefault) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiTypeSlotDTO(PokeApiNamedResourceDTO type) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiAbilitySlotDTO(PokeApiNamedResourceDTO ability) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiStatSlotDTO(@JsonProperty("base_stat") Integer baseStat, PokeApiNamedResourceDTO stat) {}
}
