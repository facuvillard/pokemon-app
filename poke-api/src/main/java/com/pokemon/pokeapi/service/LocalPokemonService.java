package com.pokemon.pokeapi.service;

import com.pokemon.pokeapi.dto.pokemon.LocalPokemonDTO;
import com.pokemon.pokeapi.dto.pokemon.UpdatePokemonDTO;
import com.pokemon.pokeapi.exception.BadRequestException;
import com.pokemon.pokeapi.exception.ResourceNotFoundException;
import com.pokemon.pokeapi.mapper.PokemonMapper;
import com.pokemon.pokeapi.model.Pokemon;
import com.pokemon.pokeapi.repository.PokemonRepository;
import com.pokemon.pokeapi.utils.PokeApiMapper;
import com.pokemon.pokeapi.client.PokeApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LocalPokemonService {

    private final PokemonRepository pokemonRepository;
    private final PokeApiClient pokeApiClient;
    private final PokemonMapper pokemonMapper;

    public LocalPokemonDTO syncPokemon(long id) {
        if (pokemonRepository.existsById(id)) {
            throw new BadRequestException("Pokemon already synced");
        }

        try {
            var data = pokeApiClient.getPokemonById(id);
            var species = pokeApiClient.getPokemonSpecies(id);
            String description = PokeApiMapper.extractEnglishDescription(species);

            Pokemon entity = PokeApiMapper.mapToPokemonEntity(data, description);
            entity.setSyncedAt(LocalDateTime.now());

            return pokemonMapper.toLocalPokemonDTO(pokemonRepository.save(entity));
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Failed to sync pokemon from external API: " + e.getMessage());
        }
    }

    public List<LocalPokemonDTO> syncPokemonBatch(List<Long> ids) {
        return ids.stream()
                .filter(id -> !pokemonRepository.existsById(id))
                .map(id -> {
                    try {
                        return syncPokemon(id);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .toList();
    }

    public Page<LocalPokemonDTO> getAllLocalPokemon(int page, int size) {
        return pokemonRepository.findAll(PageRequest.of(page, size))
                .map(pokemonMapper::toLocalPokemonDTO);
    }

    public LocalPokemonDTO getLocalPokemonById(long id) {
        Pokemon entity = pokemonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon not found with id: " + id));
        return pokemonMapper.toLocalPokemonDTO(entity);
    }

    public LocalPokemonDTO updateLocalPokemon(long id, UpdatePokemonDTO dto) {
        Pokemon entity = pokemonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon not found with id: " + id));

        if (dto.customName() != null) entity.setCustomName(dto.customName());
        if (dto.region() != null) entity.setRegion(dto.region());
        if (dto.classificationTag() != null) entity.setClassificationTag(dto.classificationTag());
        if (dto.notes() != null) entity.setNotes(dto.notes());

        entity.setUpdatedAt(LocalDateTime.now());
        return pokemonMapper.toLocalPokemonDTO(pokemonRepository.save(entity));
    }

    public void deleteLocalPokemon(long id) {
        if (!pokemonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pokemon not found with id: " + id);
        }
        pokemonRepository.deleteById(id);
    }
}
