package com.pokemon.pokeapi.utils;

import com.pokemon.pokeapi.dto.external.PokeApiEvolutionChainData;
import com.pokemon.pokeapi.dto.external.PokeApiPokemon;
import com.pokemon.pokeapi.dto.external.PokeApiSpecies;
import com.pokemon.pokeapi.dto.pokemon.EvolutionNodeDTO;
import com.pokemon.pokeapi.model.Pokemon;
import com.pokemon.pokeapi.model.PokemonStat;

import java.util.ArrayList;
import java.util.List;

public class PokeApiMapper {

    public static String extractEnglishDescription(PokeApiSpecies speciesData) {
        if (speciesData == null || speciesData.flavorTextEntries() == null) return "";
        for (var entry : speciesData.flavorTextEntries()) {
            if (entry.language() != null && "en".equals(entry.language().name())) {
                String text = entry.flavorText();
                return text != null ? text.replaceAll("[\\n\\f\\r]", " ") : "";
            }
        }
        return "";
    }

    public static List<EvolutionNodeDTO> parseEvolutionChain(PokeApiEvolutionChainData chainData) {
        List<EvolutionNodeDTO> list = new ArrayList<>();
        if (chainData == null || chainData.chain() == null) return list;
        walkEvolutionTree(chainData.chain(), list);
        return list;
    }

    private static void walkEvolutionTree(PokeApiEvolutionChainData.PokeApiChainLink node, List<EvolutionNodeDTO> list) {
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

    public static Long extractIdFromUrl(String url) {
        if (url == null) return null;
        String[] parts = url.split("/");
        try {
            return Long.parseLong(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Pokemon mapToPokemonEntity(PokeApiPokemon data, String description) {
        Long id = data.id();
        String name = data.name();
        String spriteUrl = data.sprites() != null ? data.sprites().frontDefault() : null;
        Integer weight = data.weight();
        Integer height = data.height();
        Integer baseExp = data.baseExperience();

        List<String> types = new ArrayList<>();
        if (data.types() != null) {
            for (var t : data.types()) {
                if (t.type() != null) types.add(t.type().name());
            }
        }

        List<String> abilities = new ArrayList<>();
        if (data.abilities() != null) {
            for (var a : data.abilities()) {
                if (a.ability() != null) abilities.add(a.ability().name());
            }
        }

        Pokemon pokemon = Pokemon.builder()
                .id(id)
                .name(name)
                .spriteUrl(spriteUrl)
                .weight(weight)
                .height(height)
                .baseExperience(baseExp)
                .description(description)
                .types(types)
                .abilities(abilities)
                .build();

        List<PokemonStat> stats = new ArrayList<>();
        if (data.stats() != null) {
            for (var s : data.stats()) {
                if (s.stat() != null) {
                    stats.add(PokemonStat.builder()
                            .statName(s.stat().name())
                            .baseStat(s.baseStat())
                            .pokemon(pokemon)
                            .build());
                }
            }
        }
        pokemon.setStats(stats);
        return pokemon;
    }
}
