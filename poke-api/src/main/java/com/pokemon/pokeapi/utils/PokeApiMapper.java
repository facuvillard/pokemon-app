package com.pokemon.pokeapi.utils;

import com.pokemon.pokeapi.dto.pokemon.EvolutionNodeDTO;
import com.pokemon.pokeapi.model.Pokemon;
import com.pokemon.pokeapi.model.PokemonStat;
import java.util.*;

public class PokeApiMapper {

    public static String extractEnglishDescription(Map<String, Object> speciesData) {
        if (speciesData == null || !speciesData.containsKey("flavor_text_entries")) return "";
        List<Map<String, Object>> entries = (List<Map<String, Object>>) speciesData.get("flavor_text_entries");
        for (Map<String, Object> entry : entries) {
            Map<String, Object> language = (Map<String, Object>) entry.get("language");
            if (language != null && "en".equals(language.get("name"))) {
                String text = (String) entry.get("flavor_text");
                return text != null ? text.replaceAll("[\\n\\f\\r]", " ") : "";
            }
        }
        return "";
    }

    public static List<EvolutionNodeDTO> parseEvolutionChain(Map<String, Object> chainData) {
        List<EvolutionNodeDTO> list = new ArrayList<>();
        if (chainData == null || !chainData.containsKey("chain")) return list;
        walkEvolutionTree((Map<String, Object>) chainData.get("chain"), list);
        return list;
    }

    private static void walkEvolutionTree(Map<String, Object> node, List<EvolutionNodeDTO> list) {
        if (node == null) return;
        Map<String, Object> species = (Map<String, Object>) node.get("species");
        if (species != null) {
            String name = (String) species.get("name");
            String url = (String) species.get("url");
            Long id = extractIdFromUrl(url);
            String spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";
            Integer minLevel = null;
            List<Map<String, Object>> detailsList = (List<Map<String, Object>>) node.get("evolution_details");
            if (detailsList != null && !detailsList.isEmpty()) {
                Map<String, Object> details = detailsList.get(0);
                if (details.get("min_level") != null) {
                    minLevel = ((Number) details.get("min_level")).intValue();
                }
            }
            list.add(new EvolutionNodeDTO(name, id, spriteUrl, minLevel));
        }
        List<Map<String, Object>> evolvesTo = (List<Map<String, Object>>) node.get("evolves_to");
        if (evolvesTo != null) {
            for (Map<String, Object> child : evolvesTo) {
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

    public static Pokemon mapToPokemonEntity(Map<String, Object> data, String description) {
        Long id = ((Number) data.get("id")).longValue();
        String name = (String) data.get("name");
        Map<String, Object> sprites = (Map<String, Object>) data.get("sprites");
        String spriteUrl = sprites != null ? (String) sprites.get("front_default") : null;
        Integer weight = ((Number) data.get("weight")).intValue();
        Integer height = ((Number) data.get("height")).intValue();
        Integer baseExp = ((Number) data.get("base_experience")).intValue();

        List<String> types = new ArrayList<>();
        List<Map<String, Object>> typesData = (List<Map<String, Object>>) data.get("types");
        if (typesData != null) {
            for (Map<String, Object> t : typesData) {
                Map<String, Object> typeMap = (Map<String, Object>) t.get("type");
                if (typeMap != null) types.add((String) typeMap.get("name"));
            }
        }

        List<String> abilities = new ArrayList<>();
        List<Map<String, Object>> abilitiesData = (List<Map<String, Object>>) data.get("abilities");
        if (abilitiesData != null) {
            for (Map<String, Object> a : abilitiesData) {
                Map<String, Object> abilityMap = (Map<String, Object>) a.get("ability");
                if (abilityMap != null) abilities.add((String) abilityMap.get("name"));
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
        List<Map<String, Object>> statsData = (List<Map<String, Object>>) data.get("stats");
        if (statsData != null) {
            for (Map<String, Object> s : statsData) {
                Map<String, Object> statMap = (Map<String, Object>) s.get("stat");
                Integer baseStat = ((Number) s.get("base_stat")).intValue();
                if (statMap != null) {
                    stats.add(PokemonStat.builder()
                            .statName((String) statMap.get("name"))
                            .baseStat(baseStat)
                            .pokemon(pokemon)
                            .build());
                }
            }
        }
        pokemon.setStats(stats);
        return pokemon;
    }
}
