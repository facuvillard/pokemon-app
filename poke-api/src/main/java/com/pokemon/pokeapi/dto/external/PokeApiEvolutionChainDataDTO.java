package com.pokemon.pokeapi.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.pokeapi.dto.pokemon.EvolutionNodeDTO;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokeApiEvolutionChainDataDTO(PokeApiChainLinkDTO chain) {

    public List<EvolutionNodeDTO> flattenChain() {
        List<EvolutionNodeDTO> list = new ArrayList<>();
        walkTree(chain, list);
        return list;
    }

    private void walkTree(PokeApiChainLinkDTO node, List<EvolutionNodeDTO> list) {
        if (node == null) return;
        
        if (node.species() != null) {
            String name = node.species().name();
            Long id = node.species().extractId();
            String spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";
            Integer minLevel = (node.evolutionDetails() != null && !node.evolutionDetails().isEmpty())
                    ? node.evolutionDetails().get(0).minLevel() : null;
                    
            list.add(new EvolutionNodeDTO(name, id, spriteUrl, minLevel));
        }
        
        if (node.evolvesTo() != null) {
            node.evolvesTo().forEach(child -> walkTree(child, list));
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiChainLinkDTO(
        PokeApiNamedResourceDTO species,
        @JsonProperty("evolution_details") List<PokeApiEvolutionDetailDTO> evolutionDetails,
        @JsonProperty("evolves_to") List<PokeApiChainLinkDTO> evolvesTo
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PokeApiEvolutionDetailDTO(@JsonProperty("min_level") Integer minLevel) {}
}
