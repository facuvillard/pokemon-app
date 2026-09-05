package com.pokemon.pokeapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.pokeapi.dto.pokemon.LocalPokemonDTO;
import com.pokemon.pokeapi.dto.pokemon.UpdatePokemonDTO;
import com.pokemon.pokeapi.exception.ResourceNotFoundException;
import com.pokemon.pokeapi.service.LocalPokemonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocalPokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LocalPokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LocalPokemonService localPokemonService;

    @MockBean
    private com.pokemon.pokeapi.security.JwtAuthFilter jwtAuthFilter;
    @MockBean
    private com.pokemon.pokeapi.security.JwtUtils jwtUtils;
    @MockBean
    private com.pokemon.pokeapi.security.CustomUserDetailsService customUserDetailsService;

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "testuser")
    void testSyncPokemon_Returns201() throws Exception {
        when(localPokemonService.syncPokemon(1L, "testuser")).thenReturn(mock(LocalPokemonDTO.class));
        mockMvc.perform(post("/api/v1/local/pokemon/sync/1"))
               .andExpect(status().isCreated());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "testuser")
    void testGetLocalPokemon_Returns200() throws Exception {
        when(localPokemonService.getLocalPokemonById(1L, "testuser")).thenReturn(mock(LocalPokemonDTO.class));
        mockMvc.perform(get("/api/v1/local/pokemon/1"))
               .andExpect(status().isOk());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "testuser")
    void testUpdateLocalPokemon_Returns200() throws Exception {
        UpdatePokemonDTO dto = new UpdatePokemonDTO("c", "r", "t", "n");
        when(localPokemonService.updateLocalPokemon(eq(1L), any(UpdatePokemonDTO.class), eq("testuser"))).thenReturn(mock(LocalPokemonDTO.class));
        
        mockMvc.perform(put("/api/v1/local/pokemon/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "testuser")
    void testUpdateLocalPokemon_NotFound_Returns404() throws Exception {
        UpdatePokemonDTO dto = new UpdatePokemonDTO("c", "r", "t", "n");
        when(localPokemonService.updateLocalPokemon(eq(1L), any(), eq("testuser"))).thenThrow(new ResourceNotFoundException("Not found"));
        
        mockMvc.perform(put("/api/v1/local/pokemon/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isNotFound());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "testuser")
    void testDeleteLocalPokemon_Returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/local/pokemon/1"))
               .andExpect(status().isNoContent());
    }
}
