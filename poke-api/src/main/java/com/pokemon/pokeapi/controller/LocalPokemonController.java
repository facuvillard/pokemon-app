package com.pokemon.pokeapi.controller;

import com.pokemon.pokeapi.dto.pokemon.LocalPokemonDTO;
import com.pokemon.pokeapi.dto.pokemon.SyncBatchRequestDTO;
import com.pokemon.pokeapi.dto.pokemon.UpdatePokemonDTO;
import com.pokemon.pokeapi.service.LocalPokemonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/local/pokemon")
@RequiredArgsConstructor
public class LocalPokemonController {

    private final LocalPokemonService localPokemonService;

    @PostMapping("/sync/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public LocalPokemonDTO syncPokemon(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        return localPokemonService.syncPokemon(id, userDetails.getUsername());
    }

    @PostMapping("/sync/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public java.util.List<LocalPokemonDTO> syncBatch(@Valid @RequestBody SyncBatchRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
        return localPokemonService.syncPokemonBatch(request.ids(), userDetails.getUsername());
    }

    @GetMapping("/synced-ids")
    public java.util.List<Long> getSyncedIds(@AuthenticationPrincipal UserDetails userDetails) {
        return localPokemonService.getSyncedPokemonIds(userDetails.getUsername());
    }

    @GetMapping
    public Page<LocalPokemonDTO> listLocalPokemon(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @AuthenticationPrincipal UserDetails userDetails) {
        return localPokemonService.getAllLocalPokemon(page, size, sortBy, sortDir, userDetails.getUsername());
    }

    @GetMapping("/search")
    public Page<LocalPokemonDTO> searchLocalPokemon(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @AuthenticationPrincipal UserDetails userDetails) {
        return localPokemonService.searchLocalPokemon(query, page, size, sortBy, sortDir, userDetails.getUsername());
    }

    @GetMapping("/{id}")
    public LocalPokemonDTO getLocalPokemonById(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        return localPokemonService.getLocalPokemonById(id, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    public LocalPokemonDTO updateLocalPokemon(@PathVariable long id, @Valid @RequestBody UpdatePokemonDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        return localPokemonService.updateLocalPokemon(id, dto, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocalPokemon(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        localPokemonService.deleteLocalPokemon(id, userDetails.getUsername());
    }
}
