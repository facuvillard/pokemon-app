package com.pokemon.pokeapi.dto.pokemon;

import java.time.LocalDateTime;
import java.util.List;

public record LocalPokemonDTO(
    Long id, 
    String name, 
    String spriteUrl, 
    Integer height, 
    Integer weight, 
    Integer baseExperience, 
    List<String> types, 
    List<String> abilities, 
    List<StatDTO> stats, 
    String description, 
    String customName, 
    String region, 
    String classificationTag, 
    String notes, 
    LocalDateTime syncedAt, 
    LocalDateTime updatedAt
) {}
