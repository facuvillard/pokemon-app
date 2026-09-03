package com.pokemon.pokeapi.service;

import com.pokemon.pokeapi.dto.external.PokeApiListResponseDTO;
import com.pokemon.pokeapi.dto.external.PokeApiPokemonDTO;
import com.pokemon.pokeapi.dto.external.PokeApiSpeciesDTO;
import com.pokemon.pokeapi.dto.external.PokeApiEvolutionChainDataDTO;
import com.pokemon.pokeapi.dto.pokemon.*;
import com.pokemon.pokeapi.exception.ResourceNotFoundException;
import com.pokemon.pokeapi.utils.PokeApiMapper;
import com.pokemon.pokeapi.client.PokeApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PokemonService {

    private final PokeApiClient pokeApiClient;
    private final PokeApiMapper pokeApiMapper;

    public PokemonListResponseDTO listPokemon(int page, int size) {
        int offset = page * size;
        PokeApiListResponseDTO response = pokeApiClient.getPokemonList(offset, size);
        if (response == null) {
            return new PokemonListResponseDTO(List.of(), page, size, 0, 0);
        }

        long totalElements = response.count() != null ? response.count().longValue() : 0;
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<PokemonListItemDTO> content = new ArrayList<>();

        if (response.results() != null) {
            for (var item : response.results()) {
                String url = item.url();
                Long id = pokeApiMapper.extractIdFromUrl(url);
                if (id != null) {
                    try {
                        PokeApiPokemonDTO detail = pokeApiClient.getPokemonById(id);
                        content.add(mapToListItemDTO(detail));
                    } catch (Exception e) {
                        // Skip if detail fails
                    }
                }
            }
        }

        return new PokemonListResponseDTO(content, page, size, totalElements, totalPages);
    }

    public PokemonDetailDTO getPokemonDetail(long id) {
        try {
            PokeApiPokemonDTO data = pokeApiClient.getPokemonById(id);
            PokeApiSpeciesDTO species = pokeApiClient.getPokemonSpecies(id);
            
            String description = pokeApiMapper.extractEnglishDescription(species);
            
            List<EvolutionNodeDTO> evolutionChain = new ArrayList<>();
            if (species != null && species.evolutionChain() != null && species.evolutionChain().url() != null) {
                String url = species.evolutionChain().url();
                PokeApiEvolutionChainDataDTO chainData = pokeApiClient.getEvolutionChain(url);
                evolutionChain = pokeApiMapper.parseEvolutionChain(chainData);
            }

            return mapToDetailDTO(data, description, evolutionChain);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Pokemon not found with id: " + id);
        }
    }

    private PokemonListItemDTO mapToListItemDTO(PokeApiPokemonDTO data) {
        Long id = data.id();
        String name = data.name();
        String spriteUrl = data.sprites() != null ? data.sprites().frontDefault() : null;
        Integer weight = data.weight();

        List<String> types = new ArrayList<>();
        if (data.types() != null) {
            for (var t : data.types()) {
                if (t.type() != null) types.add(t.type().name());
            }
        }

        List<String> abilities = new ArrayList<>();
        if (data.abilities() != null) {
            for (var a : data.abilities()) {
                if (a.ability() != null) abilities.add(a.ability().name());
            }
        }

        return new PokemonListItemDTO(id, name, spriteUrl, types, weight, abilities);
    }

    private PokemonDetailDTO mapToDetailDTO(PokeApiPokemonDTO data, String description, List<EvolutionNodeDTO> evolutionChain) {
        Long id = data.id();
        String name = data.name();
        String spriteUrl = data.sprites() != null ? data.sprites().frontDefault() : null;
        Integer weight = data.weight();
        Integer height = data.height();
        Integer baseExp = data.baseExperience();

        List<String> types = new ArrayList<>();
        if (data.types() != null) {
            for (var t : data.types()) {
                if (t.type() != null) types.add(t.type().name());
            }
        }

        List<String> abilities = new ArrayList<>();
        if (data.abilities() != null) {
            for (var a : data.abilities()) {
                if (a.ability() != null) abilities.add(a.ability().name());
            }
        }

        List<StatDTO> stats = new ArrayList<>();
        if (data.stats() != null) {
            for (var s : data.stats()) {
                if (s.stat() != null) {
                    stats.add(new StatDTO(s.stat().name(), s.baseStat()));
                }
            }
        }

        return new PokemonDetailDTO(id, name, spriteUrl, height, weight, baseExp, types, abilities, stats, description, evolutionChain);
    }
}
