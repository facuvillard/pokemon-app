package com.pokemon.pokeapi.dto.pokemon;

import java.util.List;

public record PokemonListResponseDTO(
    List<PokemonListItemDTO> content, 
    int page, 
    int size, 
    long totalElements, 
    int totalPages
) {}
