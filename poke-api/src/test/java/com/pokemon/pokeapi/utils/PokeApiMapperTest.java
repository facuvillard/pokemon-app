package com.pokemon.pokeapi.utils;

import com.pokemon.pokeapi.dto.pokemon.EvolutionNodeDTO;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class PokeApiMapperTest {

    @Test
    void testExtractEnglishDescription_ReturnsEnglish() {
        Map<String, Object> species = Map.of(
            "flavor_text_entries", List.of(
                Map.of("language", Map.of("name", "es"), "flavor_text", "Hola"),
                Map.of("language", Map.of("name", "en"), "flavor_text", "Hello\nWorld")
            )
        );
        String desc = PokeApiMapper.extractEnglishDescription(species);
        assertEquals("Hello World", desc);
    }

    @Test
    void testExtractEnglishDescription_NoEnglish_ReturnsDefault() {
        Map<String, Object> species = Map.of(
            "flavor_text_entries", List.of(
                Map.of("language", Map.of("name", "es"), "flavor_text", "Hola")
            )
        );
        assertEquals("", PokeApiMapper.extractEnglishDescription(species));
    }

    @Test
    void testParseEvolutionChain_ReturnsCorrectList() {
        Map<String, Object> chain = Map.of(
            "chain", Map.of(
                "species", Map.of("name", "pichu", "url", "https://pokeapi.co/api/v2/pokemon-species/172/"),
                "evolution_details", List.of(),
                "evolves_to", List.of(
                    Map.of(
                        "species", Map.of("name", "pikachu", "url", "https://pokeapi.co/api/v2/pokemon-species/25/"),
                        "evolution_details", List.of(Map.of("min_level", 16)),
                        "evolves_to", List.of()
                    )
                )
            )
        );
        
        List<EvolutionNodeDTO> res = PokeApiMapper.parseEvolutionChain(chain);
        assertEquals(2, res.size());
        assertEquals("pichu", res.get(0).name());
        assertEquals(172L, res.get(0).id());
        assertEquals("pikachu", res.get(1).name());
        assertEquals(16, res.get(1).minLevel());
    }

    @Test
    void testExtractIdFromUrl_ReturnsCorrectId() {
        assertEquals(1L, PokeApiMapper.extractIdFromUrl("https://pokeapi.co/api/v2/pokemon-species/1/"));
        assertNull(PokeApiMapper.extractIdFromUrl("invalid"));
    }
}
