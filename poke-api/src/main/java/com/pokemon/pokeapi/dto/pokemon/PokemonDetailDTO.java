package com.pokemon.pokeapi.dto.pokemon;

import java.util.List;

public record PokemonDetailDTO(
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
    List<EvolutionNodeDTO> evolutionChain
) {}
