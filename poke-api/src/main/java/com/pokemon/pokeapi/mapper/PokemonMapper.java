package com.pokemon.pokeapi.mapper;

import com.pokemon.pokeapi.dto.pokemon.LocalPokemonDTO;
import com.pokemon.pokeapi.dto.pokemon.StatDTO;
import com.pokemon.pokeapi.model.Pokemon;
import com.pokemon.pokeapi.model.PokemonStat;
import com.pokemon.pokeapi.model.UserPokemon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PokemonMapper {

    default LocalPokemonDTO toLocalPokemonDTO(UserPokemon up) {
        if (up == null) {
            return null;
        }
        Pokemon pokemon = up.getPokemon();
        return new LocalPokemonDTO(
            pokemon != null ? pokemon.getId() : null,
            pokemon != null ? pokemon.getName() : null,
            pokemon != null ? pokemon.getSpriteUrl() : null,
            pokemon != null ? pokemon.getHeight() : null,
            pokemon != null ? pokemon.getWeight() : null,
            pokemon != null ? pokemon.getBaseExperience() : null,
            pokemon != null ? pokemon.getTypes() : null,
            pokemon != null ? pokemon.getAbilities() : null,
            pokemon != null ? toStatDTOList(pokemon.getStats()) : null,
            pokemon != null ? pokemon.getDescription() : null,
            up.getCustomName(),
            up.getRegion(),
            up.getClassificationTag(),
            up.getNotes(),
            up.getSyncedAt(),
            up.getUpdatedAt()
        );
    }

    @Mapping(source = "statName", target = "name")
    StatDTO toStatDTO(PokemonStat stat);

    List<StatDTO> toStatDTOList(List<PokemonStat> stats);
}
