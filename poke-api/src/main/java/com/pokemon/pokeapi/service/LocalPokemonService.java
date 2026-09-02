package com.pokemon.pokeapi.service;

import com.pokemon.pokeapi.dto.pokemon.LocalPokemonDTO;
import com.pokemon.pokeapi.dto.pokemon.StatDTO;
import com.pokemon.pokeapi.dto.pokemon.UpdatePokemonDTO;
import com.pokemon.pokeapi.exception.BadRequestException;
import com.pokemon.pokeapi.exception.ResourceNotFoundException;
import com.pokemon.pokeapi.model.Pokemon;
import com.pokemon.pokeapi.repository.PokemonRepository;
import com.pokemon.pokeapi.utils.PokeApiMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalPokemonService {

    private final PokemonRepository pokemonRepository;
    private final PokeApiService pokeApiService;

    public LocalPokemonDTO syncPokemon(long id) {
        if (pokemonRepository.existsById(id)) {
            throw new BadRequestException("Pokemon already synced");
        }

        try {
            Map<String, Object> data = pokeApiService.getPokemonById(id);
            Map<String, Object> species = pokeApiService.getPokemonSpecies(id);
            String description = PokeApiMapper.extractEnglishDescription(species);
            
            Pokemon entity = PokeApiMapper.mapToPokemonEntity(data, description);
            entity.setSyncedAt(LocalDateTime.now());
            
            return mapToLocalPokemonDTO(pokemonRepository.save(entity));
        } catch (Exception e) {
            throw new BadRequestException("Failed to sync pokemon from external API");
        }
    }

    public void syncPokemonBatch(List<Long> ids) {
        for (Long id : ids) {
            if (!pokemonRepository.existsById(id)) {
                try {
                    syncPokemon(id);
                } catch (Exception e) {
                    // Ignore failures in batch
                }
            }
        }
    }

    public Page<LocalPokemonDTO> getAllLocalPokemon(int page, int size) {
        return pokemonRepository.findAll(PageRequest.of(page, size)).map(this::mapToLocalPokemonDTO);
    }

    public LocalPokemonDTO getLocalPokemonById(long id) {
        Pokemon entity = pokemonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon not found with id: " + id));
        return mapToLocalPokemonDTO(entity);
    }

    public LocalPokemonDTO updateLocalPokemon(long id, UpdatePokemonDTO dto) {
        Pokemon entity = pokemonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon not found with id: " + id));
        
        if (dto.customName() != null) entity.setCustomName(dto.customName());
        if (dto.region() != null) entity.setRegion(dto.region());
        if (dto.classificationTag() != null) entity.setClassificationTag(dto.classificationTag());
        if (dto.notes() != null) entity.setNotes(dto.notes());
        
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToLocalPokemonDTO(pokemonRepository.save(entity));
    }

    public void deleteLocalPokemon(long id) {
        if (!pokemonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pokemon not found with id: " + id);
        }
        pokemonRepository.deleteById(id);
    }

    private LocalPokemonDTO mapToLocalPokemonDTO(Pokemon entity) {
        List<StatDTO> stats = entity.getStats() != null ? 
            entity.getStats().stream().map(s -> new StatDTO(s.getStatName(), s.getBaseStat())).collect(Collectors.toList()) : 
            new ArrayList<>();
            
        return new LocalPokemonDTO(
            entity.getId(),
            entity.getName(),
            entity.getSpriteUrl(),
            entity.getHeight(),
            entity.getWeight(),
            entity.getBaseExperience(),
            entity.getTypes(),
            entity.getAbilities(),
            stats,
            entity.getDescription(),
            entity.getCustomName(),
            entity.getRegion(),
            entity.getClassificationTag(),
            entity.getNotes(),
            entity.getSyncedAt(),
            entity.getUpdatedAt()
        );
    }
}
