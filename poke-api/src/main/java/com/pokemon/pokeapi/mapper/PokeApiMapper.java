package com.pokemon.pokeapi.mapper;

import com.pokemon.pokeapi.dto.external.PokeApiEvolutionChainDataDTO;
import com.pokemon.pokeapi.dto.external.PokeApiPokemonDTO;
import com.pokemon.pokeapi.dto.external.PokeApiSpeciesDTO;
import com.pokemon.pokeapi.dto.pokemon.EvolutionNodeDTO;
import com.pokemon.pokeapi.model.Pokemon;
import com.pokemon.pokeapi.model.PokemonStat;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PokeApiMapper {

    public String extractEnglishDescription(PokeApiSpeciesDTO speciesData) {
        if (speciesData == null || speciesData.flavorTextEntries() == null) return "";
        for (var entry : speciesData.flavorTextEntries()) {
            if (entry.language() != null && "en".equals(entry.language().name())) {
                String text = entry.flavorText();
                return text != null ? text.replaceAll("[\\n\\f\\r]", " ") : "";
            }
        }
        return "";
    }

    public List<EvolutionNodeDTO> parseEvolutionChain(PokeApiEvolutionChainDataDTO chainData) {
        List<EvolutionNodeDTO> list = new ArrayList<>();
        if (chainData == null || chainData.chain() == null) return list;
        walkEvolutionTree(chainData.chain(), list);
        return list;
    }

    private void walkEvolutionTree(PokeApiEvolutionChainDataDTO.PokeApiChainLinkDTO node, List<EvolutionNodeDTO> list) {
        if (node == null) return;
        var species = node.species();
        if (species != null) {
            String name = species.name();
            String url = species.url();
            Long id = extractIdFromUrl(url);
            String spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";
            Integer minLevel = null;
            if (node.evolutionDetails() != null && !node.evolutionDetails().isEmpty()) {
                var details = node.evolutionDetails().get(0);
                if (details.minLevel() != null) {
                    minLevel = details.minLevel();
                }
            }
            list.add(new EvolutionNodeDTO(name, id, spriteUrl, minLevel));
        }
        if (node.evolvesTo() != null) {
            for (var child : node.evolvesTo()) {
                walkEvolutionTree(child, list);
            }
        }
    }

    public Long extractIdFromUrl(String url) {
        if (url == null) return null;
        String[] parts = url.split("/");
        try {
            return Long.parseLong(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

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
