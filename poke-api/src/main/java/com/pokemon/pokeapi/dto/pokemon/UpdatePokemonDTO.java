package com.pokemon.pokeapi.dto.pokemon;

import jakarta.validation.constraints.Size;

public record UpdatePokemonDTO(
    @Size(max = 100) String customName, 
    @Size(max = 50) String region, 
    @Size(max = 50) String classificationTag, 
    @Size(max = 500) String notes
) {}
