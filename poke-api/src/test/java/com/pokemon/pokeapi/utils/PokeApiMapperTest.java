package com.pokemon.pokeapi.utils;

import com.pokemon.pokeapi.dto.external.PokeApiEvolutionChainData;
import com.pokemon.pokeapi.dto.external.PokeApiNamedResource;
import com.pokemon.pokeapi.dto.external.PokeApiSpecies;
import com.pokemon.pokeapi.dto.pokemon.EvolutionNodeDTO;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PokeApiMapperTest {

    @Test
    void testExtractEnglishDescription_ReturnsEnglish() {
        PokeApiSpecies species = new PokeApiSpecies(
            List.of(
                new PokeApiSpecies.PokeApiFlavorText("Hola", new PokeApiNamedResource("es", null)),
                new PokeApiSpecies.PokeApiFlavorText("Hello\nWorld", new PokeApiNamedResource("en", null))
            ),
            null
        );
        String desc = PokeApiMapper.extractEnglishDescription(species);
        assertEquals("Hello World", desc);
    }

    @Test
    void testExtractEnglishDescription_NoEnglish_ReturnsDefault() {
        PokeApiSpecies species = new PokeApiSpecies(
            List.of(
                new PokeApiSpecies.PokeApiFlavorText("Hola", new PokeApiNamedResource("es", null))
            ),
            null
        );
        assertEquals("", PokeApiMapper.extractEnglishDescription(species));
    }

    @Test
    void testParseEvolutionChain_ReturnsCorrectList() {
        PokeApiEvolutionChainData.PokeApiChainLink pikachu = new PokeApiEvolutionChainData.PokeApiChainLink(
            new PokeApiNamedResource("pikachu", "https://pokeapi.co/api/v2/pokemon-species/25/"),
            List.of(new PokeApiEvolutionChainData.PokeApiEvolutionDetail(16)),
            null
        );

        PokeApiEvolutionChainData.PokeApiChainLink pichu = new PokeApiEvolutionChainData.PokeApiChainLink(
            new PokeApiNamedResource("pichu", "https://pokeapi.co/api/v2/pokemon-species/172/"),
            List.of(),
            List.of(pikachu)
        );

        PokeApiEvolutionChainData chain = new PokeApiEvolutionChainData(pichu);
        
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
