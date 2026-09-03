package com.pokemon.pokeapi.utils;

import com.pokemon.pokeapi.dto.external.PokeApiEvolutionChainDataDTO;
import com.pokemon.pokeapi.dto.external.PokeApiNamedResourceDTO;
import com.pokemon.pokeapi.dto.external.PokeApiSpeciesDTO;
import com.pokemon.pokeapi.dto.pokemon.EvolutionNodeDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PokeApiMapperTest {

    private final PokeApiMapper pokeApiMapper = Mappers.getMapper(PokeApiMapper.class);

    @Test
    void testExtractEnglishDescription_ReturnsEnglish() {
        PokeApiSpeciesDTO species = new PokeApiSpeciesDTO(
            List.of(
                new PokeApiSpeciesDTO.PokeApiFlavorTextDTO("Hola", new PokeApiNamedResourceDTO("es", null)),
                new PokeApiSpeciesDTO.PokeApiFlavorTextDTO("Hello\nWorld", new PokeApiNamedResourceDTO("en", null))
            ),
            null
        );
        String desc = pokeApiMapper.extractEnglishDescription(species);
        assertEquals("Hello World", desc);
    }

    @Test
    void testExtractEnglishDescription_NoEnglish_ReturnsDefault() {
        PokeApiSpeciesDTO species = new PokeApiSpeciesDTO(
            List.of(
                new PokeApiSpeciesDTO.PokeApiFlavorTextDTO("Hola", new PokeApiNamedResourceDTO("es", null))
            ),
            null
        );
        assertEquals("", pokeApiMapper.extractEnglishDescription(species));
    }

    @Test
    void testParseEvolutionChain_ReturnsCorrectList() {
        PokeApiEvolutionChainDataDTO.PokeApiChainLinkDTO pikachu = new PokeApiEvolutionChainDataDTO.PokeApiChainLinkDTO(
            new PokeApiNamedResourceDTO("pikachu", "https://pokeapi.co/api/v2/pokemon-species/25/"),
            List.of(new PokeApiEvolutionChainDataDTO.PokeApiEvolutionDetailDTO(16)),
            null
        );

        PokeApiEvolutionChainDataDTO.PokeApiChainLinkDTO pichu = new PokeApiEvolutionChainDataDTO.PokeApiChainLinkDTO(
            new PokeApiNamedResourceDTO("pichu", "https://pokeapi.co/api/v2/pokemon-species/172/"),
            List.of(),
            List.of(pikachu)
        );

        PokeApiEvolutionChainDataDTO chain = new PokeApiEvolutionChainDataDTO(pichu);
        
        List<EvolutionNodeDTO> res = pokeApiMapper.parseEvolutionChain(chain);
        assertEquals(2, res.size());
        assertEquals("pichu", res.get(0).name());
        assertEquals(172L, res.get(0).id());
        assertEquals("pikachu", res.get(1).name());
        assertEquals(16, res.get(1).minLevel());
    }

    @Test
    void testExtractIdFromUrl_ReturnsCorrectId() {
        assertEquals(1L, pokeApiMapper.extractIdFromUrl("https://pokeapi.co/api/v2/pokemon-species/1/"));
        assertNull(pokeApiMapper.extractIdFromUrl("invalid"));
    }
}
