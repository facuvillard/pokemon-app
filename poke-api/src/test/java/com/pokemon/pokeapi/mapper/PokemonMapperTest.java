package com.pokemon.pokeapi.mapper;

import com.pokemon.pokeapi.dto.pokemon.LocalPokemonDTO;
import com.pokemon.pokeapi.dto.pokemon.StatDTO;
import com.pokemon.pokeapi.model.Pokemon;
import com.pokemon.pokeapi.model.PokemonStat;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PokemonMapperTest {

    private final PokemonMapper mapper = Mappers.getMapper(PokemonMapper.class);

    @Test
    void toStatDTO_MapsCorrectly() {
        PokemonStat stat = PokemonStat.builder()
                .statName("hp")
                .baseStat(45)
                .build();

        StatDTO dto = mapper.toStatDTO(stat);

        assertNotNull(dto);
        assertEquals("hp", dto.name());
        assertEquals(45, dto.baseStat());
    }

    @Test
    void toLocalPokemonDTO_MapsCorrectly() {
        PokemonStat stat = PokemonStat.builder()
                .statName("hp")
                .baseStat(45)
                .build();

        Pokemon pokemon = Pokemon.builder()
                .id(1L)
                .name("bulbasaur")
                .baseExperience(64)
                .height(7)
                .weight(69)
                .types(List.of("grass", "poison"))
                .spriteUrl("url")
                .stats(List.of(stat))
                .build();

        LocalPokemonDTO dto = mapper.toLocalPokemonDTO(pokemon);

        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals("bulbasaur", dto.name());
        assertEquals(64, dto.baseExperience());
        assertEquals(7, dto.height());
        assertEquals(69, dto.weight());
        assertEquals("grass", dto.types().get(0));
        assertEquals("url", dto.spriteUrl());
        assertNotNull(dto.stats());
        assertEquals(1, dto.stats().size());
        assertEquals("hp", dto.stats().get(0).name());
        assertEquals(45, dto.stats().get(0).baseStat());
    }
}
