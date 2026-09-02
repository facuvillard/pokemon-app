package com.pokemon.pokeapi.dto.pokemon;

import java.util.List;

public record PokemonListItemDTO(
    Long id, 
    String name, 
    String spriteUrl, 
    List<String> types, 
    Integer weight, 
    List<String> abilities
) {}
