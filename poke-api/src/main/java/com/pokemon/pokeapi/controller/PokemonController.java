package com.pokemon.pokeapi.controller;

import com.pokemon.pokeapi.dto.pokemon.PokemonDetailDTO;
import com.pokemon.pokeapi.dto.pokemon.PokemonListResponseDTO;
import com.pokemon.pokeapi.service.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pokemon")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;

    @GetMapping
    public PokemonListResponseDTO listPokemon(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return pokemonService.listPokemon(page, size);
    }

    @GetMapping("/{id}")
    public PokemonDetailDTO getPokemonDetail(@PathVariable long id) {
        return pokemonService.getPokemonDetail(id);
    }
}
