package com.pokemon.pokeapi.mapper;

import com.pokemon.pokeapi.dto.pokemon.LocalPokemonDTO;
import com.pokemon.pokeapi.dto.pokemon.StatDTO;
import com.pokemon.pokeapi.model.Pokemon;
import com.pokemon.pokeapi.model.PokemonStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PokemonMapper {

    @Mapping(source = "stats", target = "stats")
    LocalPokemonDTO toLocalPokemonDTO(Pokemon entity);

    @Mapping(source = "statName", target = "name")
    StatDTO toStatDTO(PokemonStat stat);

    List<StatDTO> toStatDTOList(List<PokemonStat> stats);
}
