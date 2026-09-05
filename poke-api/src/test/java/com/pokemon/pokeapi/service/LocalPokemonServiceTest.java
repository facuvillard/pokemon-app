package com.pokemon.pokeapi.service;

import com.pokemon.pokeapi.client.PokeApiClient;
import com.pokemon.pokeapi.dto.external.PokeApiPokemonDTO;
import com.pokemon.pokeapi.dto.external.PokeApiSpeciesDTO;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocalPokemonServiceTest {

    @Mock
    private PokemonRepository pokemonRepository;

    @Mock
    private UserPokemonRepository userPokemonRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PokeApiClient pokeApiClient;

    @Mock
    private PokemonMapper pokemonMapper;
    
    @Mock
    private PokeApiMapper pokeApiMapper;

    @InjectMocks
    private LocalPokemonService localPokemonService;

    @Test
    void testSyncPokemon_Success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        when(pokemonRepository.findById(1L)).thenReturn(Optional.empty());
        
        PokeApiPokemonDTO data = new PokeApiPokemonDTO(1L, "bulbasaur", 69, 7, 64, null, null, null, null);
        when(pokeApiClient.getPokemonById(1L)).thenReturn(data);
        PokeApiSpeciesDTO species = new PokeApiSpeciesDTO(null, null);
        when(pokeApiClient.getPokemonSpecies(1L)).thenReturn(species);
        
        Pokemon entity = Pokemon.builder().id(1L).name("bulbasaur").build();
        when(pokeApiMapper.toEntity(any(), anyString())).thenReturn(entity);
        when(pokemonRepository.save(any(Pokemon.class))).thenReturn(entity);
        
        when(userPokemonRepository.existsByUserAndPokemon(user, entity)).thenReturn(false);
        when(userPokemonRepository.save(any(UserPokemon.class))).thenAnswer(i -> i.getArguments()[0]);
        
        LocalPokemonDTO mockDto = new LocalPokemonDTO(1L, "bulbasaur", null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null);
        when(pokemonMapper.toLocalPokemonDTO(any(UserPokemon.class))).thenReturn(mockDto);

        LocalPokemonDTO result = localPokemonService.syncPokemon(1L, "testuser");
        assertEquals(1L, result.id());
        assertEquals("bulbasaur", result.name());
    }

    @Test
    void testSyncPokemon_AlreadyExists_ThrowsBadRequest() {
        User user = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        Pokemon p = new Pokemon();
        when(pokemonRepository.findById(1L)).thenReturn(Optional.of(p));
        when(userPokemonRepository.existsByUserAndPokemon(user, p)).thenReturn(true);
        
        assertThrows(BadRequestException.class, () -> localPokemonService.syncPokemon(1L, "testuser"));
    }

    @Test
    void testGetLocalPokemonById_Success() {
        User user = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        UserPokemon up = new UserPokemon();
        when(userPokemonRepository.findByUserAndPokemon_Id(user, 1L)).thenReturn(Optional.of(up));
        
        LocalPokemonDTO mockDto = new LocalPokemonDTO(1L, "bulbasaur", null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null);
        when(pokemonMapper.toLocalPokemonDTO(any(UserPokemon.class))).thenReturn(mockDto);

        LocalPokemonDTO result = localPokemonService.getLocalPokemonById(1L, "testuser");
        assertEquals(1L, result.id());
    }

    @Test
    void testGetLocalPokemonById_NotFound_ThrowsException() {
        User user = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userPokemonRepository.findByUserAndPokemon_Id(user, 1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> localPokemonService.getLocalPokemonById(1L, "testuser"));
    }

    @Test
    void testUpdateLocalPokemon_Success() {
        User user = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        UserPokemon up = new UserPokemon();
        when(userPokemonRepository.findByUserAndPokemon_Id(user, 1L)).thenReturn(Optional.of(up));
        when(userPokemonRepository.save(any(UserPokemon.class))).thenAnswer(i -> i.getArguments()[0]);
        
        LocalPokemonDTO mockDto = new LocalPokemonDTO(1L, "bulbasaur", null, 0, 0, 0, null, null, null, null, "Bulby", null, null, null, null, null);
        when(pokemonMapper.toLocalPokemonDTO(any(UserPokemon.class))).thenReturn(mockDto);

        LocalPokemonDTO result = localPokemonService.updateLocalPokemon(1L, new UpdatePokemonDTO("Bulby", "Kanto", "Seed", "Cool"), "testuser");
        assertEquals("Bulby", result.customName());
    }

    @Test
    void testUpdateLocalPokemon_NotFound_ThrowsException() {
        User user = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userPokemonRepository.findByUserAndPokemon_Id(user, 1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> localPokemonService.updateLocalPokemon(1L, new UpdatePokemonDTO(null, null, null, null), "testuser"));
    }

    @Test
    void testDeleteLocalPokemon_Success() {
        User user = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        UserPokemon up = new UserPokemon();
        when(userPokemonRepository.findByUserAndPokemon_Id(user, 1L)).thenReturn(Optional.of(up));
        
        localPokemonService.deleteLocalPokemon(1L, "testuser");
        verify(userPokemonRepository, times(1)).delete(up);
    }

    @Test
    void testDeleteLocalPokemon_NotFound_ThrowsException() {
        User user = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userPokemonRepository.findByUserAndPokemon_Id(user, 1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> localPokemonService.deleteLocalPokemon(1L, "testuser"));
    }
}
