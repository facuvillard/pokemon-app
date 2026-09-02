package com.pokemon.pokeapi.service;

import com.pokemon.pokeapi.dto.pokemon.*;
import com.pokemon.pokeapi.exception.ResourceNotFoundException;
import com.pokemon.pokeapi.utils.PokeApiMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PokemonService {

    private final PokeApiService pokeApiService;

    public PokemonListResponseDTO listPokemon(int page, int size) {
        int offset = page * size;
        Map<String, Object> response = pokeApiService.getPokemonList(offset, size);
        if (response == null) {
            return new PokemonListResponseDTO(List.of(), page, size, 0, 0);
        }

        Number countNum = (Number) response.get("count");
        long totalElements = countNum != null ? countNum.longValue() : 0;
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
        List<PokemonListItemDTO> content = new ArrayList<>();

        if (results != null) {
            for (Map<String, Object> item : results) {
                String url = (String) item.get("url");
                Long id = PokeApiMapper.extractIdFromUrl(url);
                try {
                    Map<String, Object> detail = pokeApiService.getPokemonById(id);
                    content.add(mapToListItemDTO(detail));
                } catch (Exception e) {
                    // Skip if detail fails
                }
            }
        }

        return new PokemonListResponseDTO(content, page, size, totalElements, totalPages);
    }

    public PokemonDetailDTO getPokemonDetail(long id) {
        try {
            Map<String, Object> data = pokeApiService.getPokemonById(id);
            Map<String, Object> species = pokeApiService.getPokemonSpecies(id);
            
            String description = PokeApiMapper.extractEnglishDescription(species);
            
            List<EvolutionNodeDTO> evolutionChain = new ArrayList<>();
            if (species != null && species.containsKey("evolution_chain")) {
                Map<String, Object> ec = (Map<String, Object>) species.get("evolution_chain");
                if (ec != null && ec.containsKey("url")) {
                    String url = (String) ec.get("url");
                    Map<String, Object> chainData = pokeApiService.getEvolutionChain(url);
                    evolutionChain = PokeApiMapper.parseEvolutionChain(chainData);
                }
            }

            return mapToDetailDTO(data, description, evolutionChain);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Pokemon not found with id: " + id);
        }
    }

    private PokemonListItemDTO mapToListItemDTO(Map<String, Object> data) {
        Long id = ((Number) data.get("id")).longValue();
        String name = (String) data.get("name");
        Map<String, Object> sprites = (Map<String, Object>) data.get("sprites");
        String spriteUrl = sprites != null ? (String) sprites.get("front_default") : null;
        Integer weight = ((Number) data.get("weight")).intValue();

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

        return new PokemonListItemDTO(id, name, spriteUrl, types, weight, abilities);
    }

    private PokemonDetailDTO mapToDetailDTO(Map<String, Object> data, String description, List<EvolutionNodeDTO> evolutionChain) {
        Long id = ((Number) data.get("id")).longValue();
        String name = (String) data.get("name");
        Map<String, Object> sprites = (Map<String, Object>) data.get("sprites");
        String spriteUrl = sprites != null ? (String) sprites.get("front_default") : null;
        Integer weight = ((Number) data.get("weight")).intValue();
        Integer height = ((Number) data.get("height")).intValue();
        Integer baseExp = data.get("base_experience") != null ? ((Number) data.get("base_experience")).intValue() : null;

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

        List<StatDTO> stats = new ArrayList<>();
        List<Map<String, Object>> statsData = (List<Map<String, Object>>) data.get("stats");
        if (statsData != null) {
            for (Map<String, Object> s : statsData) {
                Map<String, Object> statMap = (Map<String, Object>) s.get("stat");
                Integer baseStat = ((Number) s.get("base_stat")).intValue();
                if (statMap != null) {
                    stats.add(new StatDTO((String) statMap.get("name"), baseStat));
                }
            }
        }

        return new PokemonDetailDTO(id, name, spriteUrl, height, weight, baseExp, types, abilities, stats, description, evolutionChain);
    }
}
