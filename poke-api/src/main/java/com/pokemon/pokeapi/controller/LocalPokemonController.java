package com.pokemon.pokeapi.controller;

import com.pokemon.pokeapi.dto.pokemon.LocalPokemonDTO;
import com.pokemon.pokeapi.dto.pokemon.SyncBatchRequestDTO;
import com.pokemon.pokeapi.dto.pokemon.UpdatePokemonDTO;
import com.pokemon.pokeapi.service.LocalPokemonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/local/pokemon")
@RequiredArgsConstructor
public class LocalPokemonController {

    private final LocalPokemonService localPokemonService;

    @PostMapping("/sync/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public LocalPokemonDTO syncPokemon(@PathVariable long id) {
        return localPokemonService.syncPokemon(id);
    }

    @PostMapping("/sync/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public java.util.List<LocalPokemonDTO> syncBatch(@Valid @RequestBody SyncBatchRequestDTO request) {
        return localPokemonService.syncPokemonBatch(request.ids());
    }

    @GetMapping
    public Page<LocalPokemonDTO> listLocalPokemon(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return localPokemonService.getAllLocalPokemon(page, size, sortBy, sortDir);
    }

    @GetMapping("/search")
    public Page<LocalPokemonDTO> searchLocalPokemon(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return localPokemonService.searchLocalPokemon(query, page, size, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public LocalPokemonDTO getLocalPokemonById(@PathVariable long id) {
        return localPokemonService.getLocalPokemonById(id);
    }

    @PutMapping("/{id}")
    public LocalPokemonDTO updateLocalPokemon(@PathVariable long id, @Valid @RequestBody UpdatePokemonDTO dto) {
        return localPokemonService.updateLocalPokemon(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocalPokemon(@PathVariable long id) {
        localPokemonService.deleteLocalPokemon(id);
    }
}
