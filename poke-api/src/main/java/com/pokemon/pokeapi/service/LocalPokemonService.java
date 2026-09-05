package com.pokemon.pokeapi.service;

import com.pokemon.pokeapi.dto.pokemon.LocalPokemonDTO;
import com.pokemon.pokeapi.dto.pokemon.UpdatePokemonDTO;
import com.pokemon.pokeapi.exception.BadRequestException;
import com.pokemon.pokeapi.exception.ResourceNotFoundException;
import com.pokemon.pokeapi.mapper.PokemonMapper;
import com.pokemon.pokeapi.model.Pokemon;
import com.pokemon.pokeapi.model.User;
import com.pokemon.pokeapi.model.UserPokemon;
import com.pokemon.pokeapi.repository.PokemonRepository;
import com.pokemon.pokeapi.repository.UserPokemonRepository;
import com.pokemon.pokeapi.repository.UserRepository;
import com.pokemon.pokeapi.mapper.PokeApiMapper;
import com.pokemon.pokeapi.client.PokeApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalPokemonService {

    private final PokemonRepository pokemonRepository;
    private final UserPokemonRepository userPokemonRepository;
    private final UserRepository userRepository;
    private final PokeApiClient pokeApiClient;
    private final PokemonMapper pokemonMapper;
    private final PokeApiMapper pokeApiMapper;

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    @Transactional
    public LocalPokemonDTO syncPokemon(long id, String username) {
        User user = getUser(username);

        Pokemon pokemon = pokemonRepository.findById(id).orElseGet(() -> {
            try {
                var data = pokeApiClient.getPokemonById(id);
                var species = pokeApiClient.getPokemonSpecies(id);
                String description = species.getEnglishDescription();
                Pokemon entity = pokeApiMapper.toEntity(data, description);
                return pokemonRepository.save(entity);
            } catch (Exception e) {
                throw new BadRequestException("Failed to sync pokemon from external API: " + e.getMessage());
            }
        });

        if (userPokemonRepository.existsByUserAndPokemon(user, pokemon)) {
            throw new BadRequestException("Pokemon already synced for this user");
        }

        UserPokemon userPokemon = UserPokemon.builder()
                .user(user)
                .pokemon(pokemon)
                .syncedAt(LocalDateTime.now())
                .build();

        return pokemonMapper.toLocalPokemonDTO(userPokemonRepository.save(userPokemon));
    }

    @Transactional
    public List<LocalPokemonDTO> syncPokemonBatch(List<Long> ids, String username) {
        return ids.stream()
                .map(id -> {
                    try {
                        return syncPokemon(id, username);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .toList();
    }

    public Page<LocalPokemonDTO> getAllLocalPokemon(int page, int size, String sortBy, String sortDir, String username) {
        User user = getUser(username);
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        return userPokemonRepository.findByUser(user, org.springframework.data.domain.PageRequest.of(page, size, sort))
                .map(pokemonMapper::toLocalPokemonDTO);
    }

    public Page<LocalPokemonDTO> searchLocalPokemon(String query, int page, int size, String sortBy, String sortDir, String username) {
        User user = getUser(username);
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
        return userPokemonRepository.searchByUserAndAllFields(user, query, org.springframework.data.domain.PageRequest.of(page, size, sort))
                .map(pokemonMapper::toLocalPokemonDTO);
    }

    public LocalPokemonDTO getLocalPokemonById(long id, String username) {
        User user = getUser(username);
        UserPokemon entity = userPokemonRepository.findByUserAndPokemon_Id(user, id)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon not found with id: " + id + " for user: " + username));
        return pokemonMapper.toLocalPokemonDTO(entity);
    }

    @Transactional
    public LocalPokemonDTO updateLocalPokemon(long id, UpdatePokemonDTO dto, String username) {
        User user = getUser(username);
        UserPokemon entity = userPokemonRepository.findByUserAndPokemon_Id(user, id)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon not found with id: " + id + " for user: " + username));

        if (dto.customName() != null) entity.setCustomName(dto.customName());
        if (dto.region() != null) entity.setRegion(dto.region());
        if (dto.classificationTag() != null) entity.setClassificationTag(dto.classificationTag());
        if (dto.notes() != null) entity.setNotes(dto.notes());

        entity.setUpdatedAt(LocalDateTime.now());
        return pokemonMapper.toLocalPokemonDTO(userPokemonRepository.save(entity));
    }

    @Transactional
    public void deleteLocalPokemon(long id, String username) {
        User user = getUser(username);
        UserPokemon entity = userPokemonRepository.findByUserAndPokemon_Id(user, id)
                .orElseThrow(() -> new ResourceNotFoundException("Pokemon not found with id: " + id + " for user: " + username));
        userPokemonRepository.delete(entity);
    }
}
