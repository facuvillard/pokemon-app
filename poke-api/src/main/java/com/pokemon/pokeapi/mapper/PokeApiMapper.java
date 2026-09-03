package com.pokemon.pokeapi.mapper;

import com.pokemon.pokeapi.dto.external.PokeApiPokemonDTO;
import com.pokemon.pokeapi.model.Pokemon;
import com.pokemon.pokeapi.model.PokemonStat;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PokeApiMapper {

    @Mapping(target = "spriteUrl", source = "data.sprites.frontDefault")
    @Mapping(target = "types", source = "data.types")
    @Mapping(target = "abilities", source = "data.abilities")
    @Mapping(target = "stats", ignore = true)
    public abstract Pokemon toEntity(PokeApiPokemonDTO data, String description);

    public String mapType(PokeApiPokemonDTO.PokeApiTypeSlotDTO t) {
        return t != null && t.type() != null ? t.type().name() : null;
    }

    public String mapAbility(PokeApiPokemonDTO.PokeApiAbilitySlotDTO a) {
        return a != null && a.ability() != null ? a.ability().name() : null;
    }

    @AfterMapping
    protected void linkStats(PokeApiPokemonDTO data, @MappingTarget Pokemon pokemon) {
        if (data.stats() != null) {
            List<PokemonStat> stats = data.stats().stream()
                .filter(s -> s.stat() != null)
                .map(s -> PokemonStat.builder()
                        .statName(s.stat().name())
                        .baseStat(s.baseStat())
                        .pokemon(pokemon)
                        .build())
                .collect(Collectors.toList());
            pokemon.setStats(stats);
        }
    }
}
