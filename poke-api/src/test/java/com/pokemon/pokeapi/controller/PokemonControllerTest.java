package com.pokemon.pokeapi.controller;

import com.pokemon.pokeapi.dto.pokemon.PokemonDetailDTO;
import com.pokemon.pokeapi.dto.pokemon.PokemonListResponseDTO;
import com.pokemon.pokeapi.service.PokemonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;

    @MockBean
    private com.pokemon.pokeapi.security.JwtAuthFilter jwtAuthFilter;
    @MockBean
    private com.pokemon.pokeapi.security.JwtUtils jwtUtils;
    @MockBean
    private com.pokemon.pokeapi.security.CustomUserDetailsService customUserDetailsService;

    @Test
    void testListPokemon_Returns200() throws Exception {
        when(pokemonService.listPokemon(0, 20)).thenReturn(new PokemonListResponseDTO(List.of(), 0, 20, 0, 0));
        mockMvc.perform(get("/api/v1/pokemon"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetPokemonDetail_Returns200() throws Exception {
        when(pokemonService.getPokemonDetail(1L)).thenReturn(new PokemonDetailDTO(1L, "b", null, null, null, null, List.of(), List.of(), List.of(), null, List.of()));
        mockMvc.perform(get("/api/v1/pokemon/1"))
               .andExpect(status().isOk());
    }
}
